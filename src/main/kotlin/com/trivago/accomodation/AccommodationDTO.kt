package com.trivago.accomodation

import com.trivago.location.LocationDTO
import kotlinx.serialization.Serializable

@Serializable
data class AccommodationDTO(
    val id: Long,
    val hotelierId: Long,
    val name: String,
    val rating: Int,
    val category: String,
    val location: LocationDTO,
    val image: String,
    val reputation: Int,
    val reputationBadge: String,
    val price: Int,
    val availability: Int,
    val version: Long = 0
) {
    fun toEntity(): Accommodation {
        return Accommodation{
            hotelierId = this@AccommodationDTO.hotelierId
            name = this@AccommodationDTO.name
            rating = this@AccommodationDTO.rating
            category = this@AccommodationDTO.category
            location = this@AccommodationDTO.location.toEntity()
            image = this@AccommodationDTO.image
            reputation = this@AccommodationDTO.reputation
            price = this@AccommodationDTO.price
            availability = this@AccommodationDTO.availability
            version = this@AccommodationDTO.version
        }
    }
    companion object {
        private fun calculateBadge(reputation: Int): String {
            return when (reputation) {
                in Int.MIN_VALUE..500 -> "red"
                in 501..799 -> "yellow"
                in 800..Int.MAX_VALUE -> "green"
                else -> {
                    "red"
                }
            }
        }

        fun from(accommodation: Accommodation): AccommodationDTO {
            return AccommodationDTO(
                id = accommodation.id,
                hotelierId = accommodation.hotelierId,
                name = accommodation.name,
                rating = accommodation.rating,
                category = accommodation.category,
                location = LocationDTO.from(accommodation.location),
                image = accommodation.image,
                reputation = accommodation.reputation,
                reputationBadge = calculateBadge(accommodation.reputation),
                price = accommodation.price,
                availability = accommodation.availability,
                version = accommodation.version
            )
        }
    }
}
