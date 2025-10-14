package za.co.quantive.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import za.co.quantive.backend.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Core plugins
    configureSerialization()
    configureDatabase()
    configureKoin()
    configureSecurity()
    configureMonitoring()

    // HTTP features
    configureCORS()
    configureStatusPages()

    // Routing
    configureRouting()
}
