package za.co.quantive.shared.di

import io.ktor.client.*
import za.co.quantive.shared.config.AppConfig
import za.co.quantive.shared.network.ApiConfig
import za.co.quantive.shared.network.HttpClientFactory
import za.co.quantive.shared.network.api.AuthApiClient

/**
 * Network module for dependency injection
 * Provides HTTP client and API clients
 *
 * Note: This is a legacy helper object for manual DI.
 * For apps using Koin, use the proper Koin modules in composeApp instead.
 */
object NetworkModule {

    /**
     * Provides API configuration using the new configuration system
     * For development environment
     */
    fun provideApiConfig(): ApiConfig {
        return ApiConfig.fromAppConfig(AppConfig.development())
    }

    /**
     * Provides HTTP client
     */
    fun provideHttpClient(config: ApiConfig): HttpClient {
        return HttpClientFactory.create(config)
    }

    /**
     * Provides authenticated HTTP client
     */
    fun provideAuthenticatedHttpClient(
        config: ApiConfig,
        accessToken: String,
        refreshToken: String,
        refreshTokens: suspend () -> Pair<String, String>
    ): HttpClient {
        return HttpClientFactory.createWithAuth(
            config = config,
            accessToken = accessToken,
            refreshToken = refreshToken,
            refreshTokens = refreshTokens
        )
    }

    /**
     * Provides AuthApiClient
     */
    fun provideAuthApiClient(
        httpClient: HttpClient,
        config: ApiConfig
    ): AuthApiClient {
        return AuthApiClient(httpClient, config)
    }
}
