param(
    [string[]]$Modules = @('ServiceRegistry', 'ApiGateway'),
    [string]$JavaHome = $env:JAVA_HOME
)
$ErrorActionPreference = 'Stop'
if (!$JavaHome) {
    $jdk = Get-ChildItem 'C:/Program Files/Java' -Directory -Filter 'jdk-*' | Sort-Object Name -Descending | Select-Object -First 1
    if ($jdk) { $JavaHome = $jdk.FullName }
}
$java = if ($JavaHome) { Join-Path $JavaHome 'bin/java.exe' } else { (Get-Command java).Source }
$runtime = Join-Path $PSScriptRoot '.maven'
New-Item -ItemType Directory -Path $runtime -Force | Out-Null
if (!$env:JWT_SECRET) {
    $secretFile = Join-Path $runtime 'dev-jwt-secret.txt'
    if (!(Test-Path -LiteralPath $secretFile)) {
        $bytes = New-Object byte[] 48
        $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
        try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
        [System.IO.File]::WriteAllText($secretFile, [Convert]::ToBase64String($bytes))
    }
    $env:JWT_SECRET = [System.IO.File]::ReadAllText($secretFile).Trim()
}
foreach ($module in $Modules) {
    if ($module -notin @('ServiceRegistry','ApiGateway','CustomerManagement','InventoryManagement','SalesManagement','ServiceMangement','FileUploadAndDownload','ReportManagement','Config-Server')) { throw "Unknown module: $module" }
    $port = switch ($module) { 'ServiceRegistry' {8761} 'ApiGateway' {8765} default {$null} }
    if ($port) {
        $client = New-Object System.Net.Sockets.TcpClient
        try {
            if ($client.ConnectAsync('localhost', $port).Wait(1000) -and $client.Connected) {
                Write-Output "$module port $port is already accepting connections; skipping launch."
                continue
            }
        } catch { } finally { $client.Dispose() }
    }
    $folder = Join-Path $PSScriptRoot $module
    $jar = Get-ChildItem (Join-Path $folder 'target') -Filter '*.jar' | Select-Object -First 1
    if (!$jar) { throw "Build $module first using its mvnw.cmd package -DskipTests command." }
    $pidFile = Join-Path $runtime "$module.pid"
    if (Test-Path $pidFile) {
        $running = Get-Process -Id ([int](Get-Content $pidFile)) -ErrorAction SilentlyContinue
        if ($running -and $running.ProcessName -eq 'java') { Write-Output "$module already running (PID $($running.Id))"; continue }
    }
    $process = Start-Process -FilePath $java -ArgumentList @('-jar', ('"' + $jar.FullName + '"')) -WorkingDirectory $folder -WindowStyle Hidden -RedirectStandardOutput (Join-Path $runtime "$module.stdout.log") -RedirectStandardError (Join-Path $runtime "$module.stderr.log") -PassThru
    Set-Content -LiteralPath $pidFile -Value $process.Id
    Write-Output "$module launched (PID $($process.Id)). Logs: .maven/$module.stdout.log"
}
