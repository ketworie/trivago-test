package com.trivago.location

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.text

object Locations: Table<Location>("location") {
    val id = long("id").primaryKey().bindTo { it.id }
    val city = text("city").bindTo { it.city }
    val state = text("state").bindTo { it.state }
    val country = text("country").bindTo { it.country }
    val zipCode = int("zip_code").bindTo { it.zipCode }
    val address = text("address").bindTo { it.address }
}