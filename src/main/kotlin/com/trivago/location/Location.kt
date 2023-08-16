package com.trivago.location

import org.ktorm.entity.Entity

interface Location : Entity<Location> {
    companion object : Entity.Factory<Location>()

    val id: Long
    var city: String
    var state: String
    var country: String
    var zipCode: Int
    var address: String
}