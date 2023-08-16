package com.trivago.location

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

class LocationService(private val connection: Database) {
    fun create(location: Location): Location {
        connection.sequenceOf(Locations).add(location)
        // TODO: get id
        return location
    }

    fun delete(id: Long) {
        val location = connection.sequenceOf(Locations).find { it.id eq id } ?: return
        location.delete()
    }
}