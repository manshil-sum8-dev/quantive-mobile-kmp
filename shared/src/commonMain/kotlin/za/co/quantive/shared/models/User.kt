package za.co.quantive.shared.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Domain model for User
 */
@Serializable
data class User(
    val id: Int,
    val uuid: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isEmailVerified: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val fullName: String
        get() = "$firstName $lastName"
}

/**
 * User registration request
 */
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

/**
 * User login request
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Authentication response with tokens
 */
@Serializable
data class AuthResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long // milliseconds
)

/**
 * Token refresh request
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

/**
 * Token refresh response
 */
@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
