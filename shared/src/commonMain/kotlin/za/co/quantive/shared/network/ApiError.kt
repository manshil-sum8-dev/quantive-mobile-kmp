package za.co.quantive.shared.network

import kotlinx.serialization.Serializable

/**
 * Standard API error response
 */
@Serializable
data class ApiError(
    val error: String,
    val message: String,
    val status: Int,
    val details: Map<String, String>? = null
)

/**
 * API exceptions
 */
sealed class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data class NetworkError(override val message: String = "Network error occurred") : ApiException(message)
    data class ServerError(val code: Int, override val message: String) : ApiException(message)
    data class ClientError(val code: Int, override val message: String) : ApiException(message)
    data class UnauthorizedError(override val message: String = "Unauthorized") : ApiException(message)
    data class ValidationError(val errors: Map<String, String>) : ApiException("Validation failed")
    data class UnknownError(override val message: String = "Unknown error occurred") : ApiException(message)
}
