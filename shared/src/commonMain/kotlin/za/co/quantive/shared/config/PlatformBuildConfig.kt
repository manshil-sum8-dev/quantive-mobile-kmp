package za.co.quantive.shared.config

/**
 * Expect function to get platform-specific build configuration
 * Each platform will provide its own implementation
 */
expect fun getPlatformBuildConfig(): BuildConfig
