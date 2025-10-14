package za.co.quantive.backend.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val status: Int
)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)

            val errorResponse = when (cause) {
                is IllegalArgumentException -> ErrorResponse(
                    error = "Bad Request",
                    message = cause.message ?: "Invalid request parameters",
                    status = HttpStatusCode.BadRequest.value
                )
                is IllegalStateException -> ErrorResponse(
                    error = "Conflict",
                    message = cause.message ?: "Operation failed due to state conflict",
                    status = HttpStatusCode.Conflict.value
                )
                is NoSuchElementException -> ErrorResponse(
                    error = "Not Found",
                    message = cause.message ?: "Requested resource not found",
                    status = HttpStatusCode.NotFound.value
                )
                else -> ErrorResponse(
                    error = "Internal Server Error",
                    message = "An unexpected error occurred",
                    status = HttpStatusCode.InternalServerError.value
                )
            }

            call.respond(HttpStatusCode.fromValue(errorResponse.status), errorResponse)
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Not Found",
                    message = "The requested resource was not found",
                    status = status.value
                )
            )
        }

        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Unauthorized",
                    message = "Authentication is required to access this resource",
                    status = status.value
                )
            )
        }

        status(HttpStatusCode.Forbidden) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Forbidden",
                    message = "You don't have permission to access this resource",
                    status = status.value
                )
            )
        }
    }
}
