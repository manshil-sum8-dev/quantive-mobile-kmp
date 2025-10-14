package za.co.quantive.app.data.datasource

import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.LoginRequest
import za.co.quantive.shared.models.RegisterRequest
import za.co.quantive.shared.network.api.AuthApiClient
import za.co.quantive.shared.utils.Result

/**
 * Remote data source for authentication operations
 * Handles all authentication-related API calls
 */
interface AuthRemoteDataSource : RemoteDataSource {
    suspend fun register(request: RegisterRequest): Result<AuthResponse>
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    suspend fun logout(): Result<Unit>
}

/**
 * Implementation of AuthRemoteDataSource using AuthApiClient
 */
class AuthRemoteDataSourceImpl(
    private val authApiClient: AuthApiClient
) : AuthRemoteDataSource {

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return authApiClient.register(request)
    }

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return authApiClient.login(request)
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return authApiClient.refreshToken(refreshToken)
    }

    override suspend fun logout(): Result<Unit> {
        return authApiClient.logout()
    }
}
