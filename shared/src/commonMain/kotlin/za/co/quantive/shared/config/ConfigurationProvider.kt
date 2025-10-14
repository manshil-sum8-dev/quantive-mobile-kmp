package za.co.quantive.shared.config

/**
 * Provides centralized access to application configuration
 * Singleton that holds the current configuration based on build settings
 */
object ConfigurationProvider {
    private var _buildConfig: BuildConfig? = null
    private var _appConfig: AppConfig? = null

    /**
     * Initialize the configuration provider
     * Should be called once at app startup
     */
    fun initialize(buildConfig: BuildConfig = getPlatformBuildConfig()) {
        _buildConfig = buildConfig
        _appConfig = AppConfig.forEnvironment(buildConfig.environment)
    }

    /**
     * Get the current build configuration
     * @throws IllegalStateException if not initialized
     */
    val buildConfig: BuildConfig
        get() = _buildConfig ?: throw IllegalStateException(
            "ConfigurationProvider not initialized. Call initialize() first."
        )

    /**
     * Get the current app configuration
     * @throws IllegalStateException if not initialized
     */
    val appConfig: AppConfig
        get() = _appConfig ?: throw IllegalStateException(
            "ConfigurationProvider not initialized. Call initialize() first."
        )

    /**
     * Check if the provider has been initialized
     */
    val isInitialized: Boolean
        get() = _buildConfig != null && _appConfig != null

    /**
     * Override the configuration for testing purposes
     * Should only be used in tests
     */
    fun overrideForTesting(
        buildConfig: BuildConfig = DefaultBuildConfig(),
        appConfig: AppConfig = AppConfig.development()
    ) {
        _buildConfig = buildConfig
        _appConfig = appConfig
    }

    /**
     * Reset the configuration provider
     * Primarily for testing
     */
    fun reset() {
        _buildConfig = null
        _appConfig = null
    }
}
