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
            filtered = filtered.filter { it.locationId inList connection.from(Locations).select(Locations.id).where(Locations.city eq c) }
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

    fun create(accommodation: Accommodation): Accommodation {
        connection.sequenceOf(Locations).add(accommodation.location)
        accommodations.add(accommodation)
        return accommodation
    }

    fun delete(id: Long) {
        accommodations.removeIf { it.id eq id }
    }

    fun update(accommodation: Accommodation): Accommodation {
        var dbLocationId: Long = 0
        connection
            .from(Accommodations)
            .select(Accommodations.locationId)
            .where(Accommodations.id eq accommodation.id)
            .forEach {
                dbLocationId = it[Accommodations.locationId] ?: 0
            }
        var locationId = dbLocationId
        val hasLocationChanged = dbLocationId != accommodation.location.id
        if (hasLocationChanged) {
            locationId = locationService.create(accommodation.location).id
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
                (Accommodations.version eq accommodation.version) and (Accommodations.id eq accommodation.id)
            }
        }
        if (i == 0) {
            if (hasLocationChanged) locationService.delete(locationId)
            throw RuntimeException("couldn't update accommodation. Wrong version or accommodation doesn't exist")
        }
        locationService.delete(dbLocationId)
        return accommodation
    }

    fun book(id: Long) {
        val i = connection.update(Accommodations) {
            set(Accommodations.availability, Accommodations.availability.minus(1))
            where {
                Accommodations.availability gt 0
            }
        }
        if (i == 0) throw RuntimeException("unable to book, availability is 0 or accommodation doesn't exist")
    }
}