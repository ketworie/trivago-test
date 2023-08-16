package com.trivago.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    val id: Long,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: Int,
    val address: String
) {

    fun toEntity(): Location {
        return Location{
            city = this@LocationDTO.city
            state = this@LocationDTO.state
            country = this@LocationDTO.country
            zipCode = this@LocationDTO.zipCode
            address = this@LocationDTO.address
        }
    }
    companion object {
        fun from(location: Location): LocationDTO {
            return LocationDTO(
                id = location.id,
                city = location.city,
                state = location.state,
                country = location.country,
                zipCode = location.zipCode,
                address = location.address
            )
        }
    }
}
