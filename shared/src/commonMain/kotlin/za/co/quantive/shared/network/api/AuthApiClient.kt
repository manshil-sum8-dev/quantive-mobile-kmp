package za.co.quantive.shared.network.api

import io.ktor.client.*
import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.LoginRequest
import za.co.quantive.shared.models.RegisterRequest
import za.co.quantive.shared.network.ApiConfig
import za.co.quantive.shared.network.ApiEndpoints
import za.co.quantive.shared.network.BaseApiClient
import za.co.quantive.shared.utils.Result

/**
 * API client for authentication-related operations
 */
class AuthApiClient(
    httpClient: HttpClient,
    config: ApiConfig
) : BaseApiClient(httpClient, config) {

    /**
     * Registers a new user
     *
     * @param request Registration request with user details
     * @return Result with auth response or error
     */
    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return post(ApiEndpoints.AUTH_REGISTER, request)
    }

    /**
     * Logs in an existing user
     *
     * @param request Login request with credentials
     * @return Result with auth response or error
     */
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return post(ApiEndpoints.AUTH_LOGIN, request)
    }

    /**
     * Refreshes access token using refresh token
     *
     * @param refreshToken Current refresh token
     * @return Result with new auth tokens or error
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return post(
            "${ApiEndpoints.API_V1}/auth/refresh",
            mapOf("refreshToken" to refreshToken)
        )
    }

    /**
     * Logs out the current user
     *
     * @return Result indicating success or error
     */
    suspend fun logout(): Result<Unit> {
        return post(
            "${ApiEndpoints.API_V1}/auth/logout",
            emptyMap<String, String>()
        )
    }
}
