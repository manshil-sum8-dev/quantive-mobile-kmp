# Quantive Backend

Modern Kotlin backend built with Ktor 3.1, Exposed ORM, and PostgreSQL.

## Tech Stack

- **Framework**: Ktor 3.1.0
- **Language**: Kotlin 2.2.20
- **Database**: PostgreSQL 16
- **ORM**: Exposed 0.57.0
- **Connection Pool**: HikariCP 6.2.1
- **Migrations**: Flyway 11.0.3
- **Serialization**: kotlinx.serialization 1.8.0
- **DI**: Koin 4.1.0
- **Authentication**: JWT (Auth0 JWT)
- **Security**: BCrypt for password hashing

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/za/co/quantive/backend/
│   │   │   ├── Application.kt          # Main entry point
│   │   │   ├── plugins/                # Ktor plugins configuration
│   │   │   │   ├── Database.kt         # Database & connection pool setup
│   │   │   │   ├── Security.kt         # JWT authentication
│   │   │   │   ├── Serialization.kt    # JSON serialization
│   │   │   │   ├── CORS.kt             # CORS configuration
│   │   │   │   ├── Monitoring.kt       # Logging & monitoring
│   │   │   │   ├── StatusPages.kt      # Error handling
│   │   │   │   ├── Koin.kt             # Dependency injection
│   │   │   │   └── Routing.kt          # API routes
│   │   │   ├── routes/                 # REST API endpoints
│   │   │   ├── rpc/                    # RPC services (kotlinx.rpc)
│   │   │   ├── domain/                 # Business logic (use cases)
│   │   │   ├── data/                   # Repository implementations
│   │   │   ├── database/
│   │   │   │   └── tables/             # Exposed table definitions
│   │   │   ├── di/                     # Koin modules
│   │   │   └── utils/                  # Utility functions
│   │   └── resources/
│   │       ├── application.conf        # Ktor configuration
│   │       ├── logback.xml             # Logging configuration
│   │       └── db/migration/           # Flyway SQL migrations
│   └── test/                           # Unit and integration tests
└── build.gradle.kts
```

## Getting Started

### Prerequisites

- JDK 17 or higher
- Docker & Docker Compose (for local PostgreSQL)
- Gradle 8.x (wrapper included)

### Local Development Setup

#### 1. Start PostgreSQL Database

From the project root directory:

```bash
docker-compose up -d postgres
```

This will start PostgreSQL on `localhost:5432` with:
- Database: `quantive_dev`
- Username: `quantive`
- Password: `quantive_dev_pass`

#### 2. Run Database Migrations

Migrations run automatically on application startup via Flyway.

#### 3. Start the Backend Server

From the project root:

```bash
# Run in development mode
./gradlew :backend:run

# Or with hot reload
./gradlew :backend:run --continuous
```

The server will start on `http://localhost:8080`

#### 4. Verify Installation

Check the health endpoint:

```bash
curl http://localhost:8080/health
```

Expected response:
```json
{
  "status": "healthy",
  "service": "quantive-backend",
  "version": "0.0.1"
}
```

### Environment Configuration

The application reads configuration from `application.conf` which supports environment variable overrides.

#### Development Configuration (Hardcoded)

All development secrets are hardcoded in `application.conf`:
- Database credentials
- JWT secrets
- Default configuration values

#### Production/Staging Configuration (Environment Variables)

For production and staging, override via environment variables:

```bash
# Database
export DATABASE_HOST=your-db-host
export DATABASE_PORT=5432
export DATABASE_NAME=quantive_prod
export DATABASE_USER=quantive_user
export DATABASE_PASSWORD=secure_password_from_github_secrets

# JWT Configuration
export JWT_SECRET=super_secure_256_bit_secret_from_github_secrets
export JWT_ISSUER=quantive
export JWT_AUDIENCE=quantive-api
export JWT_ACCESS_TOKEN_VALIDITY=3600000  # 1 hour
export JWT_REFRESH_TOKEN_VALIDITY=2592000000  # 30 days

# Server
export PORT=8080
```

**GitHub Actions Integration:**

Environment variables will be injected from GitHub Secrets in CI/CD pipelines.

## Database Schema

### Current Tables

1. **users** - User accounts
   - id, uuid, email, password_hash
   - first_name, last_name
   - is_email_verified
   - created_at, updated_at, deleted_at (soft delete)

2. **refresh_tokens** - JWT refresh tokens
   - id, user_id, token
   - expires_at, is_revoked
   - created_at

3. **audit_logs** - Security audit trail
   - id, user_id, action
   - entity_type, entity_id
   - metadata (JSONB), ip_address, user_agent
   - created_at

### Running Migrations Manually

```bash
./gradlew :backend:flywayMigrate
```

### Creating New Migrations

Create a new SQL file in `src/main/resources/db/migration/`:

```
V{version}__{description}.sql
Example: V4__Add_invoices_table.sql
```

## API Endpoints

### Public Endpoints

- `GET /health` - Health check

### Authentication (Coming Soon)

- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token

### Protected Endpoints (Coming Soon)

All protected endpoints require JWT Bearer token:

```
Authorization: Bearer <your_jwt_token>
```

## Building for Production

### Create Fat JAR

```bash
./gradlew :backend:buildFatJar
```

Output: `backend/build/libs/quantive-backend.jar`

### Run Production JAR

```bash
java -jar backend/build/libs/quantive-backend.jar
```

## Testing

```bash
# Run all tests
./gradlew :backend:test

# Run tests with coverage
./gradlew :backend:test :backend:jacocoTestReport
```

## Logging

Logs are configured in `logback.xml`:

- Console output (STDOUT)
- File output (`logs/quantive-backend.log`)
- Rolling policy: Daily rotation, 30 days retention, 1GB max

### Log Levels

- `za.co.quantive`: DEBUG
- `io.ktor`: INFO
- `org.jetbrains.exposed`: DEBUG

## Security Considerations

### Development Mode

- CORS is set to `anyHost()` for development
- Database password is hardcoded
- JWT secret is a simple dev secret

### Production Mode

**⚠️ IMPORTANT: Before deploying to production:**

1. ✅ Use strong, randomly generated JWT secrets (256+ bits)
2. ✅ Restrict CORS to specific origins
3. ✅ Use environment variables for all secrets
4. ✅ Enable HTTPS/TLS
5. ✅ Configure rate limiting
6. ✅ Set up database backups
7. ✅ Use connection pooling with appropriate limits
8. ✅ Enable audit logging for all financial transactions
9. ✅ Implement request signing for RPC calls
10. ✅ Never store raw payment card data (PCI-DSS)

## Architecture Decisions

### Why Ktor?

- Native Kotlin framework with coroutines support
- Lightweight and performant
- Excellent KMP integration
- Modular plugin architecture

### Why Exposed?

- Type-safe SQL DSL
- Kotlin-first design
- Lightweight compared to Hibernate
- Perfect for financial applications requiring precise control

### Why REST + RPC?

- **REST**: Simple CRUD operations (invoices, contacts, products)
- **RPC (kotlinx.rpc)**: Complex business logic (cash flow analytics, predictions)
- Type-safe contracts across mobile and backend

### Why PostgreSQL?

- ACID compliance critical for financial data
- JSONB support for flexible metadata
- Excellent performance and reliability
- Rich ecosystem and tooling

## Next Steps

1. Implement authentication endpoints (register, login, refresh)
2. Add user repository and use cases
3. Create invoice management endpoints
4. Set up kotlinx.rpc services for analytics
5. Implement rate limiting
6. Add comprehensive test coverage
7. Set up CI/CD pipeline
8. Create API documentation (OpenAPI/Swagger)

## Troubleshooting

### Database Connection Issues

```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Check logs
docker logs quantive-postgres

# Restart database
docker-compose restart postgres
```

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill process or change port in application.conf
```

### Migration Failures

```bash
# Check migration status
./gradlew :backend:flywayInfo

# Repair migration history
./gradlew :backend:flywayRepair

# Clean and re-run (⚠️ DESTRUCTIVE - dev only)
./gradlew :backend:flywayClean :backend:flywayMigrate
```

## Contributing

1. Follow Kotlin coding conventions
2. Write tests for new features
3. Update documentation
4. Use meaningful commit messages
5. Create feature branches

## License

Proprietary - Quantive © 2025
