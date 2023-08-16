package com.trivago.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        swaggerUI(path = "api/doc")
    }
}
