# Data Layer - Repository Pattern

This directory contains the data layer implementation following the Repository pattern.

## Architecture Overview

```
Domain Layer (Business Logic)
    ↓
Repository Interface (Contract)
    ↓
Repository Implementation (Data Layer)
    ↓
Data Sources (Remote/Local)
    ↓
API Clients / Database
```

## Components

### 1. Data Sources

Data sources are responsible for fetching data from a specific source (network, database, cache).

**Remote Data Sources** (`datasource/`)
- Handle network API calls
- Use API clients from shared module
- Return `Result<T>` for error handling

**Local Data Sources** (`datasource/`)
- Handle local database operations
- Manage cache
- Currently not implemented (no offline support)

Example:
```kotlin
interface AuthRemoteDataSource : RemoteDataSource {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
}

class AuthRemoteDataSourceImpl(
    private val authApiClient: AuthApiClient
) : AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return authApiClient.login(request)
    }
}
```

### 2. Repositories

Repositories are the single source of truth for data. They:
- Coordinate between multiple data sources
- Implement business logic for data operations
- Cache data if needed
- Provide clean API to domain layer

The repository interface is defined in the **domain layer** to maintain dependency inversion.

Example:
```kotlin
// Domain layer - interface
interface AuthRepository : Repository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
}

// Data layer - implementation
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        val request = LoginRequest(email, password)
        return remoteDataSource.login(request)
    }
}
```

## Data Flow

1. **ViewModel/UseCase** → Calls repository method
2. **Repository** → Coordinates data sources and applies business logic
3. **Data Source** → Fetches data from API/Database
4. **Repository** → Returns `Result<T>` back to caller

## Error Handling

All repository methods return `Result<T>` which can be:
- `Result.Success(data)` - Operation succeeded
- `Result.Error(exception, message)` - Operation failed
- `Result.Loading` - Operation in progress

## Dependency Injection

Repositories and data sources are provided via Koin:

```kotlin
val repositoryModule = module {
    // Data Sources
    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(authApiClient = get())
    }

    // Repositories
    single<AuthRepository> {
        AuthRepositoryImpl(remoteDataSource = get())
    }
}
```

## Best Practices

1. **Single Responsibility**: Each repository handles one domain entity
2. **Interface Segregation**: Repository interfaces are in the domain layer
3. **Testability**: Easy to mock repositories for testing
4. **Error Handling**: Always return `Result<T>`
5. **Caching**: Repository decides caching strategy
6. **Offline Support**: Repository coordinates remote and local data sources

## Example Usage

```kotlin
// In a Use Case
class LoginUseCase(
    private val authRepository: AuthRepository
) : UseCase<LoginUseCase.Params, AuthResponse>() {
    override suspend fun execute(params: Params): Result<AuthResponse> {
        // Validation logic
        if (params.email.isBlank()) {
            return Result.Error(...)
        }

        // Delegate to repository
        return authRepository.login(params.email, params.password)
    }
}
```

## Adding New Repositories

1. Create data source interface and implementation in `data/datasource/`
2. Create repository interface in `domain/repository/`
3. Create repository implementation in `data/repository/`
4. Register in `di/RepositoryModule.kt`
5. Create use cases that use the repository

## Structure

```
data/
├── datasource/
│   ├── RemoteDataSource.kt          # Base interface
│   ├── LocalDataSource.kt           # Base interface
│   ├── AuthRemoteDataSource.kt      # Auth data source
│   └── ...                          # Other data sources
└── repository/
    ├── AuthRepositoryImpl.kt        # Auth repository impl
    └── ...                          # Other repository impls

domain/
├── repository/
│   ├── Repository.kt                # Base marker interface
│   ├── AuthRepository.kt            # Auth repository interface
│   └── ...                          # Other repository interfaces
└── usecase/
    ├── UseCase.kt                   # Base use case
    ├── LoginUseCase.kt              # Login use case
    └── ...                          # Other use cases
```
