package com.trivago

import com.trivago.accomodation.AccomodationService
import com.trivago.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.ktorm.database.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val connect = Database.connect("jdbc:postgresql://postgres:5432/trivago", user = "trivago", password = "changeit")
    val accomodationService = AccomodationService(connect)
    configureHTTP()
    configureRouting()
}
