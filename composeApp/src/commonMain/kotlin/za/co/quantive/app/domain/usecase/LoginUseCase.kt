package za.co.quantive.app.domain.usecase

import za.co.quantive.app.domain.repository.AuthRepository
import za.co.quantive.shared.models.AuthResponse
import za.co.quantive.shared.utils.Result

/**
 * Use case for user login
 * Encapsulates the business logic for logging in a user
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) : UseCase<LoginUseCase.Params, AuthResponse>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun execute(params: Params): Result<AuthResponse> {
        // Add any business logic/validation here
        if (params.email.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Email cannot be empty"),
                message = "Please enter your email address"
            )
        }

        if (params.password.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Password cannot be empty"),
                message = "Please enter your password"
            )
        }

        // Delegate to repository
        return authRepository.login(
            email = params.email,
            password = params.password
        )
    }
}
