package za.co.quantive.shared.network

import za.co.quantive.shared.config.AppConfig

/**
 * API configuration for different environments
 * Integrates with the centralized AppConfig system
 */
data class ApiConfig(
    val baseUrl: String,
    val timeout: Long = 30_000, // 30 seconds
    val enableLogging: Boolean = false
) {
    companion object {
        /**
         * Create ApiConfig from centralized AppConfig
         * This is the recommended way to create ApiConfig
         */
        fun fromAppConfig(appConfig: AppConfig): ApiConfig {
            return ApiConfig(
                baseUrl = appConfig.fullApiUrl,
                timeout = appConfig.requestTimeout,
                enableLogging = appConfig.enableNetworkLogging
            )
        }

        /**
         * @deprecated Use fromAppConfig() with AppConfig.development() instead
         */
        @Deprecated(
            message = "Use fromAppConfig() with AppConfig.development() instead",
            replaceWith = ReplaceWith("fromAppConfig(AppConfig.development())")
        )
        fun development() = ApiConfig(
            baseUrl = "http://localhost:8080",
            enableLogging = true
        )

        /**
         * @deprecated Use fromAppConfig() with AppConfig.staging() instead
         */
        @Deprecated(
            message = "Use fromAppConfig() with AppConfig.staging() instead",
            replaceWith = ReplaceWith("fromAppConfig(AppConfig.staging())")
        )
        fun staging() = ApiConfig(
            baseUrl = "https://staging-api.quantive.co.za",
            enableLogging = true
        )

        /**
         * @deprecated Use fromAppConfig() with AppConfig.production() instead
         */
        @Deprecated(
            message = "Use fromAppConfig() with AppConfig.production() instead",
            replaceWith = ReplaceWith("fromAppConfig(AppConfig.production())")
        )
        fun production() = ApiConfig(
            baseUrl = "https://api.quantive.co.za",
            enableLogging = false
        )
    }
}

/**
 * API endpoints
 */
object ApiEndpoints {
    // Base paths
    const val API_V1 = "/api/v1"

    // Authentication
    const val AUTH = "$API_V1/auth"
    const val AUTH_REGISTER = "$AUTH/register"
    const val AUTH_LOGIN = "$AUTH/login"
    const val AUTH_REFRESH = "$AUTH/refresh"
    const val AUTH_LOGOUT = "$AUTH/logout"

    // User
    const val USERS = "$API_V1/users"
    const val USER_ME = "$USERS/me"

    // Health
    const val HEALTH = "/health"
}
