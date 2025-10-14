package za.co.quantive.app.domain.repository

import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.models.User
import za.co.quantive.shared.utils.Result

/**
 * Repository interface for authentication operations
 * Defines the contract for authentication-related data operations
 */
interface AuthRepository : Repository {
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<AuthResponse>

    suspend fun login(email: String, password: String): Result<AuthResponse>

    suspend fun logout(): Result<Unit>

    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>

    suspend fun getCurrentUser(): Result<User?>

    suspend fun isLoggedIn(): Boolean
}
