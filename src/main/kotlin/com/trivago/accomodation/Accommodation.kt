package com.trivago.accomodation

import com.trivago.location.Location
import org.ktorm.entity.Entity

interface Accommodation : Entity<Accommodation> {
    companion object : Entity.Factory<Accommodation>()
    val id: Long
    var hotelierId: Long
    var name: String
    var rating: Int
    var category: String
    var location: Location
    var image: String
    var reputation: Int
    var price: Int
    var availability: Int
    var version: Long

}
