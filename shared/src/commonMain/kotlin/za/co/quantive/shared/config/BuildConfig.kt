package za.co.quantive.shared.config

/**
 * Platform-specific build configuration
 * Each platform will provide its own implementation
 */
interface BuildConfig {
    /**
     * Current environment the app is running in
     * Determined at build time
     */
    val environment: Environment

    /**
     * Application version name (e.g., "1.0.0")
     */
    val versionName: String

    /**
     * Application version code (e.g., 1)
     */
    val versionCode: Int

    /**
     * Build timestamp
     */
    val buildTime: Long

    /**
     * Is this a debug build?
     */
    val isDebug: Boolean
}

/**
 * Default build configuration for development
 */
class DefaultBuildConfig(
    override val environment: Environment = Environment.DEVELOPMENT,
    override val versionName: String = "1.0.0-dev",
    override val versionCode: Int = 1,
    override val buildTime: Long = 0L,
    override val isDebug: Boolean = true
) : BuildConfig
