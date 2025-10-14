package za.co.quantive.app.testing

import kotlinx.datetime.Clock
import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.User

/**
 * Test data factory for creating test objects
 * Provides consistent test data across all tests
 */
object TestData {

    /**
     * Create a test user
     */
    fun createUser(
        id: Int = 1,
        uuid: String = "test-user-uuid",
        email: String = "test@example.com",
        firstName: String = "Test",
        lastName: String = "User",
        isEmailVerified: Boolean = false
    ): User {
        val now = Clock.System.now()
        return User(
            id = id,
            uuid = uuid,
            email = email,
            firstName = firstName,
            lastName = lastName,
            isEmailVerified = isEmailVerified,
            createdAt = now,
            updatedAt = now
        )
    }

    /**
     * Create a test auth response
     */
    fun createAuthResponse(
        user: User = createUser(),
        accessToken: String = "test-access-token",
        refreshToken: String = "test-refresh-token",
        expiresIn: Long = 3600000L // 1 hour in milliseconds
    ): AuthResponse {
        return AuthResponse(
            user = user,
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn
        )
    }
}
