package za.co.quantive.shared.config

/**
 * Main application configuration
 * Provides environment-specific settings for the entire application
 */
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
) {
    companion object {
        /**
         * Development configuration
         * - Localhost API
         * - Verbose logging enabled
         * - No crash reporting
         * - All features enabled
         */
        fun development(): AppConfig {
            return AppConfig(
                environment = Environment.DEVELOPMENT,
                apiBaseUrl = "http://localhost:8080",
                apiVersion = "v1",
                enableLogging = true,
                enableNetworkLogging = true,
                enableCrashReporting = false,
                requestTimeout = 60_000L, // 60 seconds for debugging
                connectTimeout = 30_000L,
                features = FeatureFlags.allEnabled()
            )
        }

        /**
         * Staging configuration
         * - Staging server
         * - Moderate logging
         * - Crash reporting enabled
         * - All features enabled for testing
         */
        fun staging(): AppConfig {
            return AppConfig(
                environment = Environment.STAGING,
                apiBaseUrl = "https://staging-api.quantive.co.za",
                apiVersion = "v1",
                enableLogging = true,
                enableNetworkLogging = true,
                enableCrashReporting = true,
                requestTimeout = 30_000L,
                connectTimeout = 15_000L,
                features = FeatureFlags.allEnabled()
            )
        }

        /**
         * Production configuration
         * - Production server
         * - Minimal logging
         * - Crash reporting enabled
         * - Features controlled by remote config
         */
        fun production(): AppConfig {
            return AppConfig(
                environment = Environment.PRODUCTION,
                apiBaseUrl = "https://api.quantive.co.za",
                apiVersion = "v1",
                enableLogging = false,
                enableNetworkLogging = false,
                enableCrashReporting = true,
                requestTimeout = 30_000L,
                connectTimeout = 15_000L,
                features = FeatureFlags.production()
            )
        }

        /**
         * Create configuration based on environment
         */
        fun forEnvironment(environment: Environment): AppConfig {
            return when (environment) {
                Environment.DEVELOPMENT -> development()
                Environment.STAGING -> staging()
                Environment.PRODUCTION -> production()
            }
        }
    }

    /**
     * Full API base URL
     */
    val fullApiUrl: String
        get() = "$apiBaseUrl/api/$apiVersion"
}
