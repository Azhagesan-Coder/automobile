param([string[]]$Modules = @('ApiGateway','CustomerManagement','InventoryManagement','SalesManagement','ServiceMangement','FileUploadAndDownload','ReportManagement','Config-Server','ServiceRegistry'), [string]$JavaHome = $env:JAVA_HOME, [switch]$Offline)
$ErrorActionPreference = 'Stop'
if ($JavaHome) { $env:JAVA_HOME = $JavaHome }
elseif (Test-Path 'C:/Program Files/Java') {
    $installedJdk = Get-ChildItem 'C:/Program Files/Java' -Directory -Filter 'jdk-*' | Sort-Object Name -Descending | Select-Object -First 1
    if ($installedJdk) { $env:JAVA_HOME = $installedJdk.FullName }
}
$env:MAVEN_USER_HOME = Join-Path $PSScriptRoot '.maven'
# Mockito in the older Spring Boot modules needs this when running on JDK 25.
$env:MAVEN_OPTS = '-Dnet.bytebuddy.experimental=true'
$failures = @()
foreach ($module in $Modules) {
    Push-Location (Join-Path $PSScriptRoot $module)
    try {
        $offlineArguments = @()
        if ($Offline) { $offlineArguments = @('-o') }
        & ./mvnw.cmd @offlineArguments -B -ntp "-Dmaven.repo.local=$PSScriptRoot/.maven/repository" '-Dnet.bytebuddy.experimental=true' '-Dtest=!*ApplicationTests' '-Dsurefire.failIfNoSpecifiedTests=false' package *> (Join-Path $PSScriptRoot "$module-build.log")
        $buildCode = $LASTEXITCODE
        Write-Output "$module exit=$buildCode"
        Get-Content (Join-Path $PSScriptRoot "$module-build.log") -Tail 12
        if ($buildCode -ne 0) { $failures += $module }
    } finally { Pop-Location }
}
if ($failures.Count -gt 0) { throw "Failed modules: $($failures -join ', ')" }
