package za.co.quantive.app.data.repository

import za.co.quantive.app.data.datasource.AuthRemoteDataSource
import za.co.quantive.app.domain.repository.AuthRepository
import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.LoginRequest
import za.co.quantive.shared.models.RegisterRequest
import za.co.quantive.shared.models.User
import za.co.quantive.shared.utils.Result

/**
 * Implementation of AuthRepository
 * Coordinates between data sources and provides data to the domain layer
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    // In-memory storage for current user and tokens
    // In a real app, this would use secure storage (KeyStore/Keychain)
    private var currentUser: User? = null
    private var accessToken: String? = null
    private var refreshTokenValue: String? = null

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<AuthResponse> {
        val request = RegisterRequest(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName
        )

        return when (val result = remoteDataSource.register(request)) {
            is Result.Success -> {
                // Store user and tokens
                currentUser = result.data.user
                accessToken = result.data.accessToken
                refreshTokenValue = result.data.refreshToken
                result
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        val request = LoginRequest(email = email, password = password)

        return when (val result = remoteDataSource.login(request)) {
            is Result.Success -> {
                // Store user and tokens
                currentUser = result.data.user
                accessToken = result.data.accessToken
                refreshTokenValue = result.data.refreshToken
                result
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun logout(): Result<Unit> {
        return when (val result = remoteDataSource.logout()) {
            is Result.Success -> {
                // Clear stored data
                currentUser = null
                accessToken = null
                refreshTokenValue = null
                result
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return when (val result = remoteDataSource.refreshToken(refreshToken)) {
            is Result.Success -> {
                // Update tokens
                accessToken = result.data.accessToken
                refreshTokenValue = result.data.refreshToken
                currentUser = result.data.user
                result
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return Result.Success(currentUser)
    }

    override suspend fun isLoggedIn(): Boolean {
        return currentUser != null && accessToken != null
    }
}
