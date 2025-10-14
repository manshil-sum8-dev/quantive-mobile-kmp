package za.co.quantive.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
    val version: String
)

fun Application.configureRouting() {
    routing {
        // Health check endpoint
        get("/health") {
            call.respond(
                HealthResponse(
                    status = "healthy",
                    service = "quantive-backend",
                    version = "0.0.1"
                )
            )
        }

        // API v1 routes
        route("/api/v1") {
            // Auth routes will go here
            // route("/auth") { ... }

            // Protected routes will go here
            // authenticate("auth-jwt") { ... }
        }
    }
}
