package za.co.quantive.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO

        // Log request details
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val userAgent = call.request.headers["User-Agent"]

            "Status: $status, HTTP method: $httpMethod, Path: $path, User agent: $userAgent"
        }

        // Filter out health check endpoints from logs
        filter { call ->
            !call.request.path().startsWith("/health")
        }
    }
}
