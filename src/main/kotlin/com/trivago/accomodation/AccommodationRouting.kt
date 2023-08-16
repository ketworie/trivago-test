package com.trivago.accomodation

import com.trivago.location.LocationDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAccommodationRouting(accommodationService: AccommodationService) {
    routing {
        get("/accommodations") {
            val hotelierId = call.request.queryParameters["hotelierId"]?.toLongOrNull()
                ?: throw RuntimeException("hotelierId is missing or not a number")
            val rating = call.request.queryParameters["rating"]?.toIntOrNull()
            val city = call.request.queryParameters["city"]
            val reputationBadge = call.request.queryParameters["reputationBadge"]
            val accommodations = accommodationService.getAll(hotelierId, rating, city, reputationBadge)
            call.respond(accommodations.map { AccommodationDTO.from(it) })
        }
        get("/accommodations/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw RuntimeException("id is missing or is not a number")
            val accommodation = accommodationService.getById(id)
            call.respond(AccommodationDTO.from(accommodation))
        }
        post("/accommodations") {
            val dto = call.receive<AccommodationDTO>()
            val accommodation = accommodationService.create(dto.toEntity())
            call.respond(AccommodationDTO.from(accommodation))
        }
        put("/accommodations") {
            val dto = call.receive<AccommodationDTO>()
            val accommodation = accommodationService.update(dto.toEntity())
            call.respond(AccommodationDTO.from(accommodation))
        }
        delete("/accommodations/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw RuntimeException("id is missing or is not a number")
            accommodationService.delete(id)
            call.respond(HttpStatusCode.OK, "")
        }
        post("/accommodations/{id}/book") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw RuntimeException("id is missing or is not a number")
            accommodationService.book(id)
            call.respond(HttpStatusCode.OK, "")
        }

        post("/accommodations/generate") {
            accommodationService.create(
                AccommodationDTO(
                    id = 0,
                    hotelierId = 1,
                    name = "Test1",
                    rating = 1,
                    category = "resort",
                    location = LocationDTO(0, "Los Angeles", "California", "USA", 10000, "Some st. 5"),
                    image = "http://aaa.com/image.png",
                    reputation = 3,
                    reputationBadge = "",
                    price = 100,
                    availability = 10,
                    version = 0
                ).toEntity()
            )
            accommodationService.create(
                AccommodationDTO(
                    id = 0,
                    hotelierId = 1,
                    name = "Test2",
                    rating = 2,
                    category = "hotel",
                    location = LocationDTO(0, "Austin", "Texas", "USA", 10001, "Some st. 6"),
                    image = "http://aab.com/image.png",
                    reputation = 4,
                    reputationBadge = "",
                    price = 200,
                    availability = 20,
                    version = 0
                ).toEntity()
            )
            accommodationService.create(
                AccommodationDTO(
                    id = 0,
                    hotelierId = 2,
                    name = "Test1",
                    rating = 1,
                    category = "resort",
                    location = LocationDTO(0, "Los Angeles", "California", "USA", 10002, "Some st. 77"),
                    image = "http://aac.com/image.png",
                    reputation = 900,
                    reputationBadge = "",
                    price = 1000,
                    availability = 1000,
                    version = 0
                ).toEntity()
            )
            call.respond(HttpStatusCode.OK, "")
        }
    }
}
