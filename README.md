# AutoShowroom

Java 17 Spring Boot services for customer, inventory, sales, appointments, files and reporting.

## Angular frontend

The responsive Angular workspace is in `frontend`. Run `npm install` and `npm start` from that directory, then open http://localhost:4200. The development proxy forwards `/api` to the gateway on port 8765. Start the backend services below, then register or sign in through the UI. See [frontend setup](frontend/README.md) for features and production hosting instructions.

For local login, run `./Start-Backend.ps1` to launch the built ServiceRegistry and ApiGateway JARs in the background. MySQL must already be running with the configured security database. The launcher uses `JWT_SECRET` if set, otherwise it generates and reuses a development secret in ignored `.maven/dev-jwt-secret.txt`. Logs are under `.maven`. Build changed backend modules before using this launcher. Pass `-Modules CustomerManagement,InventoryManagement,SalesManagement,ServiceMangement,FileUploadAndDownload,ReportManagement` to launch the other built business services. If Angular logs `ECONNREFUSED` for `/api/auth/login`, check `.maven/ApiGateway.stdout.log` and confirm the gateway has started on 8765; restart `npm start` after changing the proxy.

## Configuration and startup

Create MySQL databases `security`, `customer_db`, `inventory_db`, `sales_db`, `service_db` and `files_db` (check each datasource URL if your names differ). Set `DB_PASSWORD` and, for the gateway, `JWT_SECRET` to a securely generated secret containing at least 32 UTF-8 bytes. Do not commit credentials. Previously embedded credentials must be rotated if real.

Optional SMS credentials: `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN`, `TWILIO_PHONE_NUMBER`. Sales CRUD works without SMS configuration; sending SMS requires valid credentials.

### Gateway JWT setup

The gateway reads `jwt.secret` from the `JWT_SECRET` environment variable. `HS256` is the signing algorithm name, not a valid secret. In PowerShell, generate a random secret and start the gateway from that same terminal:

```powershell
$jwtBytes = New-Object byte[] 32
$jwtRng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$jwtRng.GetBytes($jwtBytes)
$jwtRng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($jwtBytes)
Set-Location D:\Workspace1\AutoShowroom\ApiGateway
.\mvnw.cmd spring-boot:run
```

Save this generated value securely and reuse it on subsequent starts and across gateway instances. The application uses the resulting string as UTF-8 key bytes; it does not Base64-decode it. Changing the secret invalidates existing tokens. The PowerShell variable applies only to that terminal and its child processes. When starting from an IDE, set `JWT_SECRET` to the generated value in the gateway application's Run Configuration environment variables. A `.env` file is not automatically loaded. Missing or short values intentionally stop startup. If your Config Server supplies `jwt.secret`, update that configuration too.

Start Config-Server (8088), ServiceRegistry (8761), then business services and ApiGateway (8765). Run a module with its `mvnw.cmd spring-boot:run`. Business services also have local port and Eureka defaults, so an unavailable optional Config Server does not make them all fall back to port 8080. Services advertise their IP address to avoid discovery failures caused by an unresolvable machine hostname.

### Calling services through the gateway

Eureka on port 8761 is the service registry; API requests go to the gateway on port 8765. The gateway proxies requests internally, so a successful response does not redirect the browser to a service port. Keep the complete backend path, for example `http://localhost:8765/api/invent/1` or `http://localhost:8765/api/customers/1`.

Log in at `http://localhost:8765/api/auth/login`, then include `Authorization: Bearer <token>` on business requests. Direct service calls do not run gateway authentication. A gateway 401 means the token is missing, expired or invalid; 503 usually means no instance is available in discovery; 404 can mean an incorrect API path or a missing record. Check that the service appears as UP at `http://localhost:8761` and allow time for the gateway to fetch the registry after startup. Restart affected applications after changing configuration.

## API contracts

Gateway paths now match backend paths: `/api/customers`, `/api/invent`, `/api/sales`, `/api/service`, `/api/files`, `/api/report`, `/api/sms`. Old gateway prefixes such as `/customer` are replaced.

Register with `POST /api/auth/register`; login with `POST /api/auth/login`, passing JSON `username` and `password`. Use the returned token as `Authorization: Bearer <token>`. Tokens expire in one hour. This is authentication, with no administrator-specific authorization implemented.

Customers, inventory, sales and appointments have separate validated request DTOs and response DTOs with explicit mapping. Request bodies omit generated IDs. Appointment creation also omits status (set to Scheduled). Customer and vehicle PUT operations replace all fields; send a complete request. Service status values: Scheduled, In Progress, Completed, Cancelled.

| Service | DTO mapping |
| --- | --- |
| CustomerManagement | CustomerRequest → Customer → CustomerResponse; inventory JSON id/stock → carId/quantity |
| InventoryManagement | VehiclesRequest → Vehicles → VehiclesResponse |
| SalesManagement | SalesRequest → Sales → SalesResponse; CustomerReference and VehicleReference for upstream checks |
| ServiceMangement | ServiceRecordRequest → ServiceRecord → ServiceRecordResponse |
| FileUploadAndDownload | Multipart upload → FilesDocument → FileResponse (no filesystem path) |
| ReportManagement | SaleSource and VehicleSource → aggregated SalesReportDTO |
| ApiGateway | AuthRequest → User; User → AuthResponse containing only the token |

ServiceRegistry and Config-Server do not need business DTO mapping.

Required request fields: customer name/email; vehicle vin/make/model/price/category/stock; sale customerId/carId/amount/saleDate; appointment vehicleId/serviceType/appointmentDate. Numeric amounts and stock are validated. Sales IDs remain JSON strings but must contain positive numeric IDs. Responses retain their existing field names except appointment input no longer accepts status and file metadata no longer exposes filePath. Creation returns HTTP 201 for the four business DTO APIs.

Username and customer email now have database uniqueness constraints. Existing duplicate records must be resolved before applying those constraints to an existing database.

Customer inventory lookup retains `carId`/`quantity` in its public response and maps inventory's `id`/`stock` using JSON aliases. Inventory does not supply a variant, so that field remains null. File upload returns safe metadata without a server path.

Reports read `/api/sales` and resolve vehicle models from inventory through Eureka. Date ranges are inclusive. Missing vehicles are grouped as Unknown vehicle followed by ID. The unused report entity, repository and datasource configuration were removed; reports no longer need a separate database. Live report requests load all sales and are intended for modest datasets; use paginated aggregation or a durable reporting projection for larger datasets. Current vehicle names are used, not historical snapshots.

Sale creation requires numeric customer and vehicle IDs, verifies both records exist, and rejects vehicles without stock. This availability check does not reserve or deduct stock. Inventory stock, customer purchase history and SMS are not automatically changed. Cross-service transactions require a separately designed workflow to prevent duplicate stock deductions and partial updates. Business services should be reachable only on a private network because JWT enforcement is at the gateway.

## Checks

Use `mvnw.cmd test` in each module. Context-loading tests require configured MySQL and JWT environment variables. Unit tests for authentication, DTO mapping and report behavior can run independently using `-Dtest=AuthServiceTest`, `-Dtest=CustomerServiceTest,InventoryResponseTest`, or `-Dtest=ReportServiceTest` in their corresponding modules.

`./Validate.ps1` builds all nine applications and runs the regression tests, including an H2-backed authentication integration test, while excluding the original infrastructure-dependent ApplicationTests. It writes a build log per module. Use `-Offline` once dependencies are cached or `-JavaHome <JDK directory>` to select a JDK. Source compilation targets Java 17; the runner enables Byte Buddy's experimental compatibility when using newer installed JDKs.
