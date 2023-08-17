package com.trivago

import com.trivago.accomodation.AccommodationDTO
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.abs
import kotlin.math.log10

private val hotelNameForbiddenWordsPattern = "( (free|website|book|offer))|((free|website|book|offer) )".toRegex()
private val urlPattern =
    "^https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)$".toRegex()

fun Application.configureHTTP() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(RequestValidation) {
        validate(AccommodationDTO::class) {
            if (it.name.lowercase().contains(hotelNameForbiddenWordsPattern)) {
                return@validate ValidationResult.Invalid("A hotel name cannot contain the words [\"Free\", \"Offer\", \"Book\", \"Website\"]")
            }
            if (it.name.length <= 10) {
                return@validate ValidationResult.Invalid("A hotel name should be more than 10 characters")
            }
            if (it.rating > 5 || it.rating < 0) {
                return@validate ValidationResult.Invalid("Rating should be between 0 and 5")
            }
            if (!listOf("hotel", "alternative", "hostel", "lodge", "resort", "guesthouse").contains(it.category)) {
                return@validate ValidationResult.Invalid("The category can be one of [hotel, alternative, hostel, lodge, resort, guesthouse]")
            }
            val zipDigitsNumber = log10(abs(it.location.zipCode.toDouble())).toInt() + 1
            if (zipDigitsNumber != 5) {
                return@validate ValidationResult.Invalid("Zip Code should be 5 digits")
            }
            if (!urlPattern.matches(it.image)) {
                return@validate ValidationResult.Invalid("Image should be valid url")
            }
            if (it.reputation < 0 || it.reputation > 1000) {
                return@validate ValidationResult.Invalid("Reputation should be between 0 and 1000")
            }
            ValidationResult.Valid
        }
    }
    routing {
        swaggerUI(path = "api/doc")
    }
}
