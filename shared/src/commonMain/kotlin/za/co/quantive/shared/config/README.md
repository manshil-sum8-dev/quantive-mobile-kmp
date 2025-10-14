# Environment Configuration Management

This directory contains the centralized configuration system for managing different environments (development, staging, production) and their settings.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    Application Startup                           │
│  1. Platform provides BuildConfig (Android/iOS specific)        │
│  2. ConfigurationProvider.initialize(buildConfig)                │
│  3. AppConfig created based on environment                       │
│  4. Koin modules use ConfigurationProvider for dependencies      │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    ConfigurationProvider                         │
│  - Singleton holding current configuration                       │
│  - Provides BuildConfig and AppConfig                           │
│  - Initialized once at app startup                              │
└─────────────────────────────────────────────────────────────────┘
          ↓                                    ↓
┌──────────────────────┐          ┌────────────────────────────┐
│     BuildConfig      │          │       AppConfig            │
│  Platform-specific   │          │  Environment-specific      │
│  - Environment       │          │  - API URLs                │
│  - Version info      │          │  - Timeouts                │
│  - Debug flag        │          │  - Logging settings        │
└──────────────────────┘          │  - FeatureFlags            │
                                  └────────────────────────────┘
```

## Components

### 1. Environment

Enum representing different runtime environments:

```kotlin
enum class Environment {
    DEVELOPMENT,  // Local development
    STAGING,      // Pre-production testing
    PRODUCTION    // Live production
}
```

**Properties:**
- `isDevelopment`, `isStaging`, `isProduction` - Environment checks
- `isDebug` - True for development and staging

### 2. AppConfig

Main application configuration with environment-specific settings:

```kotlin
data class AppConfig(
    val environment: Environment,
    val apiBaseUrl: String,
    val apiVersion: String,
    val enableLogging: Boolean,
    val enableNetworkLogging: Boolean,
    val enableCrashReporting: Boolean,
    val requestTimeout: Long,
    val connectTimeout: Long,
    val features: FeatureFlags
)
```

**Factory Methods:**
- `AppConfig.development()` - Development configuration
- `AppConfig.staging()` - Staging configuration
- `AppConfig.production()` - Production configuration
- `AppConfig.forEnvironment(env)` - Create for specific environment

**Environment Differences:**

| Setting | Development | Staging | Production |
|---------|-------------|---------|------------|
| API URL | localhost:8080 | staging-api.quantive.co.za | api.quantive.co.za |
| Logging | Verbose | Moderate | Minimal |
| Crash Reporting | Disabled | Enabled | Enabled |
| Request Timeout | 60s | 30s | 30s |
| Features | All enabled | All enabled | Controlled |

### 3. FeatureFlags

Controls feature availability without code changes:

```kotlin
data class FeatureFlags(
    val enableBiometricAuth: Boolean,
    val enableOfflineMode: Boolean,
    val enableAnalytics: Boolean,
    val enableNotifications: Boolean,
    val enableBudgetInsights: Boolean,
    val enableExpenseCategories: Boolean,
    val enableRecurringTransactions: Boolean,
    val enableDataExport: Boolean,
    val enableDarkMode: Boolean
)
```

**Presets:**
- `FeatureFlags.allEnabled()` - All features on (dev/staging)
- `FeatureFlags.production()` - Production defaults
- `FeatureFlags.minimal()` - Core features only

### 4. BuildConfig

Platform-specific build information:

```kotlin
interface BuildConfig {
    val environment: Environment    // Current environment
    val versionName: String         // Version (e.g., "1.0.0")
    val versionCode: Int            // Build number
    val buildTime: Long             // Build timestamp
    val isDebug: Boolean            // Debug build flag
}
```

**Platform Implementations:**
- `AndroidBuildConfig` - Gets values from Android BuildConfig
- `IosBuildConfig` - Gets values from Info.plist
- `DefaultBuildConfig` - Fallback for testing

### 5. ConfigurationProvider

Singleton providing centralized access to configuration:

```kotlin
object ConfigurationProvider {
    fun initialize(buildConfig: BuildConfig)
    val buildConfig: BuildConfig
    val appConfig: AppConfig
    val isInitialized: Boolean
}
```

**Important:**
- Must be initialized before Koin starts
- Initialized in Application.onCreate() (Android) or initKoin() (iOS)
- Throws exception if accessed before initialization

## Usage

### Initialization

**Android (QuantiveApplication.kt):**
```kotlin
override fun onCreate() {
    super.onCreate()

    // Initialize configuration FIRST
    ConfigurationProvider.initialize(getPlatformBuildConfig())

    // Then start Koin
    startKoin {
        modules(appModules)
    }
}
```

**iOS (KoinInitializer.kt):**
```kotlin
fun initKoin() {
    // Initialize configuration FIRST
    ConfigurationProvider.initialize(getPlatformBuildConfig())

    // Then start Koin
    startKoin {
        modules(appModules)
    }
}
```

### Accessing Configuration

**In Koin Modules:**
```kotlin
val networkModule = module {
    // BuildConfig from ConfigurationProvider
    single<BuildConfig> {
        ConfigurationProvider.buildConfig
    }

    // AppConfig from ConfigurationProvider
    single<AppConfig> {
        ConfigurationProvider.appConfig
    }

    // ApiConfig from AppConfig
    single<ApiConfig> {
        ApiConfig.fromAppConfig(get())
    }
}
```

**In ViewModels/Use Cases (via DI):**
```kotlin
class SomeViewModel(
    private val appConfig: AppConfig
) : BaseViewModel<State, Event, Effect>(State()) {

    fun checkFeature() {
        if (appConfig.features.enableBudgetInsights) {
            // Feature is enabled
        }
    }
}
```

**Direct Access (use sparingly):**
```kotlin
fun someFunction() {
    val config = ConfigurationProvider.appConfig
    if (config.environment.isDevelopment) {
        println("Running in development mode")
    }
}
```

### Checking Features

```kotlin
class BudgetViewModel(
    private val appConfig: AppConfig
) : BaseViewModel<State, Event, Effect>(State()) {

    init {
        // Check if feature is enabled
        if (!appConfig.features.enableBudgetInsights) {
            // Navigate to disabled feature screen
            sendEffect(Effect.ShowFeatureDisabled)
        }
    }
}
```

### Conditional UI

```kotlin
@Composable
fun HomeScreen(appConfig: AppConfig = koinInject()) {
    Column {
        // Always show
        BasicFeatures()

        // Show only if enabled
        if (appConfig.features.enableBudgetInsights) {
            BudgetInsightsSection()
        }

        // Show debug info only in development
        if (appConfig.environment.isDevelopment) {
            DebugInfoPanel()
        }
    }
}
```

## Configuration Flow

### 1. App Startup
```
Platform (Android/iOS)
    ↓
getPlatformBuildConfig()
    ↓
ConfigurationProvider.initialize(buildConfig)
    ↓
AppConfig.forEnvironment(buildConfig.environment)
    ↓
Koin modules access via ConfigurationProvider
```

### 2. Runtime Access
```
Component needs config
    ↓
Inject AppConfig via Koin
    ↓
Access properties (apiBaseUrl, features, etc.)
    ↓
Make decisions based on configuration
```

## Environment Selection

### Development Builds
```kotlin
// Android: Set in build.gradle.kts
android {
    buildTypes {
        debug {
            buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
        }
    }
}

// iOS: Set in build settings
// DEBUG_ENVIRONMENT = DEVELOPMENT
```

### Staging Builds
```kotlin
// Android
android {
    buildTypes {
        create("staging") {
            buildConfigField("String", "ENVIRONMENT", "\"STAGING\"")
        }
    }
}

// iOS: Create staging scheme with build configuration
```

### Production Builds
```kotlin
// Android
android {
    buildTypes {
        release {
            buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
        }
    }
}

// iOS: Use release configuration
```

## Testing

### Override Configuration for Tests
```kotlin
@Before
fun setup() {
    ConfigurationProvider.overrideForTesting(
        buildConfig = DefaultBuildConfig(
            environment = Environment.DEVELOPMENT
        ),
        appConfig = AppConfig.development()
    )
}

@After
fun teardown() {
    ConfigurationProvider.reset()
}
```

### Test Different Environments
```kotlin
@Test
fun `test production configuration`() {
    val config = AppConfig.production()

    assertFalse(config.enableLogging)
    assertTrue(config.enableCrashReporting)
    assertFalse(config.features.enableBudgetInsights)
}
```

## Best Practices

### 1. Initialize Early
Always initialize ConfigurationProvider before Koin starts. Configuration should be the first thing initialized.

### 2. Use Dependency Injection
Prefer injecting `AppConfig` via Koin rather than accessing `ConfigurationProvider` directly.

```kotlin
// Good - DI
class MyViewModel(private val appConfig: AppConfig) : BaseViewModel(...)

// Avoid - Direct access
class MyViewModel() : BaseViewModel(...) {
    private val appConfig = ConfigurationProvider.appConfig
}
```

### 3. Check Features Before Use
Always check feature flags before using optional features:

```kotlin
if (appConfig.features.enableBudgetInsights) {
    showBudgetInsights()
}
```

### 4. Environment-Specific Logic
Use environment checks for debug/development-only code:

```kotlin
if (appConfig.environment.isDevelopment) {
    logDetailedDebugInfo()
}
```

### 5. Remote Configuration
For production, consider fetching feature flags from a remote config service:

```kotlin
class RemoteConfigRepository {
    suspend fun fetchFeatureFlags(): FeatureFlags {
        // Fetch from remote config service
    }
}
```

## Migration from Old System

The old `ApiConfig` factory methods are deprecated:

```kotlin
// Old (deprecated)
ApiConfig.development()
ApiConfig.staging()
ApiConfig.production()

// New (recommended)
val appConfig = ConfigurationProvider.appConfig
val apiConfig = ApiConfig.fromAppConfig(appConfig)

// Or via Koin (recommended)
class MyClass(private val apiConfig: ApiConfig)
```

## Structure

```
config/
├── Environment.kt              # Environment enum
├── AppConfig.kt                # Main app configuration
├── FeatureFlags.kt             # Feature flag management
├── BuildConfig.kt              # Build config interface + default
├── PlatformBuildConfig.kt      # Expect declaration
├── ConfigurationProvider.kt    # Configuration singleton
└── README.md                   # This file

config/ (platform-specific)
├── androidMain/
│   └── PlatformBuildConfig.android.kt  # Android implementation
└── iosMain/
    └── PlatformBuildConfig.ios.kt      # iOS implementation
```

## Future Enhancements

1. **Remote Configuration**
   - Fetch feature flags from backend
   - Update configuration at runtime
   - A/B testing support

2. **Configuration Persistence**
   - Cache remote configuration locally
   - Offline configuration fallbacks

3. **User-Specific Configuration**
   - Per-user feature flags
   - Beta tester access to features

4. **Analytics Integration**
   - Track feature usage
   - Monitor configuration changes

5. **Build Variants**
   - Multiple development environments
   - Customer-specific builds
   - White-label configurations
