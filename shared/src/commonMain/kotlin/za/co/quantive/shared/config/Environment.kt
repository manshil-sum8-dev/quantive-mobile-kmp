package za.co.quantive.shared.config

/**
 * Represents the different environments the app can run in
 */
enum class Environment {
    /**
     * Development environment - Local development with verbose logging
     */
    DEVELOPMENT,

    /**
     * Staging environment - Pre-production testing
     */
    STAGING,

    /**
     * Production environment - Live production environment
     */
    PRODUCTION;

    val isDevelopment: Boolean
        get() = this == DEVELOPMENT

    val isStaging: Boolean
        get() = this == STAGING

    val isProduction: Boolean
        get() = this == PRODUCTION

    val isDebug: Boolean
        get() = this == DEVELOPMENT || this == STAGING
}
