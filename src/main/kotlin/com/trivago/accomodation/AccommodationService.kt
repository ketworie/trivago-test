package com.trivago.accomodation

import com.trivago.location.LocationService
import com.trivago.location.Locations
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*

class AccommodationService(private val connection: Database, private val locationService: LocationService) {

    private val accommodations = connection.sequenceOf(Accommodations)
    private val accommodationsWithReferences = connection.sequenceOf(Accommodations, true)
    fun getAll(hotelierId: Long, rating: Int?, city: String?, reputationBadge: String?): List<Accommodation> {
        var filtered = accommodationsWithReferences.filter { it.hotelierId eq hotelierId }
        rating?.let { r ->
            filtered = filtered.filter { it.rating eq r }
        }
        city?.let { c ->
            filtered = filtered.filter {
                it.locationId inList connection.from(Locations).select(Locations.id).where(Locations.city eq c)
            }
        }
        reputationBadge?.let {
            when (it) {
                "red" -> {
                    filtered = filtered.filter { it.rating lte 500 }
                }

                "yellow" -> {
                    filtered = filtered.filter { it.rating gt 500 or (it.rating lte 799) }
                }

                "green" -> {
                    filtered = filtered.filter { it.rating gt 799 }
                }
            }
        }
        return filtered.toList()
    }

    fun getById(id: Long): Accommodation {
        return accommodationsWithReferences.find { it.id eq id }
            ?: throw RuntimeException("accommodation for id $id not found")
    }

    fun create(dto: AccommodationDTO): AccommodationDTO {
        val accommodation = dto.toEntity()
        connection.sequenceOf(Locations).add(accommodation.location)
        accommodations.add(accommodation)
        return AccommodationDTO.from(accommodation)
    }

    fun delete(id: Long) {
        accommodations.removeIf { it.id eq id }
    }

    fun update(dto: AccommodationDTO): AccommodationDTO {
        val accommodation = dto.toEntity()
        var dbLocationId: Long = 0
        connection
            .from(Accommodations)
            .select(Accommodations.locationId)
            .where(Accommodations.id eq dto.id)
            .forEach {
                dbLocationId = it[Accommodations.locationId] ?: 0
            }
        var locationId = dbLocationId
        val hasLocationChanged = dbLocationId != dto.location.id
        if (hasLocationChanged) {
            locationId = locationService.create(dto.location).id
        }
        val i = connection.update(Accommodations) {
            set(Accommodations.name, accommodation.name)
            set(Accommodations.rating, accommodation.rating)
            set(Accommodations.category, accommodation.category)
            set(Accommodations.image, accommodation.image)
            set(Accommodations.reputation, accommodation.reputation)
            set(Accommodations.price, accommodation.price)
            set(Accommodations.locationId, dbLocationId)
            set(Accommodations.availability, accommodation.availability)
            set(Accommodations.version, ++accommodation.version)
            where {
                (Accommodations.version eq dto.version) and (Accommodations.id eq dto.id)
            }
        }
        if (i == 0) {
            if (hasLocationChanged) locationService.delete(locationId)
            throw RuntimeException("couldn't update accommodation. Wrong version or accommodation doesn't exist")
        }
        if (hasLocationChanged) {
            locationService.delete(dbLocationId)
        }
        return dto.copy(location = dto.location.copy(id = locationId))
    }

    fun book(id: Long) {
        val i = connection.update(Accommodations) {
            set(Accommodations.availability, Accommodations.availability.minus(1))
            where {
                Accommodations.availability gt 0 and (Accommodations.id eq id)
            }
        }
        if (i == 0) throw RuntimeException("unable to book, availability is 0 or accommodation doesn't exist")
    }
}