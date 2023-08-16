package com.trivago.accomodation

import com.trivago.location.Locations
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.text

object Accommodations: Table<Accommodation>("accommodation") {
    val id = long("id").primaryKey().bindTo { it.id }
    val hotelierId = long("hotelier_id").bindTo { it.hotelierId }
    val name = text("name").bindTo { it.name }
    val rating = int("rating").bindTo { it.rating }
    val category = text("category").bindTo { it.category }
    val locationId = long("location_id").references(Locations) { it.location }
    val image = text("image").bindTo { it.image }
    val reputation = int("reputation").bindTo { it.reputation }
    val price = int("price").bindTo { it.price }
    val availability = int("availability").bindTo { it.availability }
    val version = long("version").bindTo { it.version }
}