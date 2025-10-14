package za.co.quantive.shared.config

/**
 * iOS-specific build configuration
 * In a real app, these values would come from Info.plist or compile-time flags
 */
class IosBuildConfig : BuildConfig {
    override val environment: Environment
        get() = when {
            // In a real app, this would check compilation flags
            isDebug -> Environment.DEVELOPMENT
            else -> Environment.PRODUCTION
        }

    override val versionName: String
        get() = "1.0.0" // Would come from CFBundleShortVersionString

    override val versionCode: Int
        get() = 1 // Would come from CFBundleVersion

    override val buildTime: Long
        get() = 0L // Would be set at compile time in production

    override val isDebug: Boolean
        get() = platform.Foundation.NSProcessInfo.processInfo.environment["DEBUG"] != null
}

/**
 * Provides the platform-specific BuildConfig instance
 */
actual fun getPlatformBuildConfig(): BuildConfig = IosBuildConfig()
