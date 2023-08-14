package com.trivago.accomodation

import org.ktorm.database.Database

class AccomodationService(val connection: Database) {
    fun test(): String {
        return "test"
    }
}