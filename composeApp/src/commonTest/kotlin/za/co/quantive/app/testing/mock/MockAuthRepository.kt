package za.co.quantive.app.testing.mock

import za.co.quantive.app.domain.repository.AuthRepository
import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.User
import za.co.quantive.shared.utils.Result

/**
 * Mock implementation of AuthRepository for testing
 * Allows controlling responses for different test scenarios
 */
class MockAuthRepository : AuthRepository {

    // Control flags for different scenarios
    var shouldReturnError = false
    var errorMessage = "Mock error"

    // Mock data
    private var mockUser: User? = null
    private var mockAccessToken: String? = null
    private var mockRefreshToken: String? = null

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<AuthResponse> {
        return if (shouldReturnError) {
            Result.Error(
                exception = Exception(errorMessage),
                message = errorMessage
            )
        } else {
            val now = kotlinx.datetime.Clock.System.now()
            val user = User(
                id = 1,
                uuid = "mock-user-uuid",
                email = email,
                firstName = firstName,
                lastName = lastName,
                isEmailVerified = false,
                createdAt = now,
                updatedAt = now
            )
            mockUser = user
            mockAccessToken = "mock-access-token"
            mockRefreshToken = "mock-refresh-token"

            Result.Success(
                AuthResponse(
                    user = user,
                    accessToken = "mock-access-token",
                    refreshToken = "mock-refresh-token",
                    expiresIn = 3600000L
                )
            )
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return if (shouldReturnError) {
            Result.Error(
                exception = Exception(errorMessage),
                message = errorMessage
            )
        } else {
            val now = kotlinx.datetime.Clock.System.now()
            val user = User(
                id = 1,
                uuid = "mock-user-uuid",
                email = email,
                firstName = "Test",
                lastName = "User",
                isEmailVerified = false,
                createdAt = now,
                updatedAt = now
            )
            mockUser = user
            mockAccessToken = "mock-access-token"
            mockRefreshToken = "mock-refresh-token"

            Result.Success(
                AuthResponse(
                    user = user,
                    accessToken = "mock-access-token",
                    refreshToken = "mock-refresh-token",
                    expiresIn = 3600000L
                )
            )
        }
    }

    override suspend fun logout(): Result<Unit> {
        return if (shouldReturnError) {
            Result.Error(
                exception = Exception(errorMessage),
                message = errorMessage
            )
        } else {
            mockUser = null
            mockAccessToken = null
            mockRefreshToken = null
            Result.Success(Unit)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return if (shouldReturnError) {
            Result.Error(
                exception = Exception(errorMessage),
                message = errorMessage
            )
        } else {
            mockUser?.let { user ->
                Result.Success(
                    AuthResponse(
                        user = user,
                        accessToken = "new-mock-access-token",
                        refreshToken = "new-mock-refresh-token",
                        expiresIn = 3600000L
                    )
                )
            } ?: Result.Error(
                exception = Exception("No user logged in"),
                message = "No user logged in"
            )
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return Result.Success(mockUser)
    }

    override suspend fun isLoggedIn(): Boolean {
        return mockUser != null
    }

    /**
     * Reset the mock to its initial state
     */
    fun reset() {
        shouldReturnError = false
        errorMessage = "Mock error"
        mockUser = null
        mockAccessToken = null
        mockRefreshToken = null
    }
}
