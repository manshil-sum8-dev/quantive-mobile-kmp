package za.co.quantive.shared.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HttpClient instances
 */
object HttpClientFactory {

    /**
     * Creates a configured HttpClient with common settings
     *
     * @param config API configuration with base URL and settings
     * @param tokenProvider Optional function to provide auth token
     * @return Configured HttpClient instance
     */
    fun create(
        config: ApiConfig,
        tokenProvider: (() -> String?)? = null
    ): HttpClient {
        return HttpClient {
            // Base URL configuration
            defaultRequest {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
            }

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = config.timeout
                connectTimeoutMillis = config.timeout
                socketTimeoutMillis = config.timeout
            }

            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            // Logging (only in development)
            if (config.enableLogging) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }

            // Authentication (if token provider is available)
            tokenProvider?.let { provider ->
                install(Auth) {
                    bearer {
                        loadTokens {
                            provider()?.let { token ->
                                BearerTokens(
                                    accessToken = token,
                                    refreshToken = "" // Will be handled separately
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a configured HttpClient with bearer token authentication
     *
     * @param config API configuration
     * @param accessToken Current access token
     * @param refreshToken Current refresh token
     * @param refreshTokens Callback to refresh tokens when they expire
     * @return Configured HttpClient with auth
     */
    fun createWithAuth(
        config: ApiConfig,
        accessToken: String,
        refreshToken: String,
        refreshTokens: suspend () -> Pair<String, String>
    ): HttpClient {
        return HttpClient {
            defaultRequest {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = config.timeout
                connectTimeoutMillis = config.timeout
                socketTimeoutMillis = config.timeout
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            if (config.enableLogging) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        val (newAccess, newRefresh) = refreshTokens()
                        BearerTokens(
                            accessToken = newAccess,
                            refreshToken = newRefresh
                        )
                    }
                }
            }
        }
    }
}
