package com.radikal.pcnotifications.model.domain

import java.util.*

/**
 * Created by tudor on 21.02.2017.
 */
class Sms {
    var address: String = ""
    var message: String = ""
    var date: Date = Date()
    var type: Int= 0

    constructor()

    constructor(address: String, message: String, date: Date, type: Int) {
        this.address = address
        this.message = message
        this.date = date
        this.type = type
    }

    override fun toString(): String {
        return "Sms(address = $address, message = $message, date = $date, type = $type)"
    }
}