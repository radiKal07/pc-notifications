package com.radikal.pcnotifications.model.domain

import java.util.*

/**
 * Created by tudor on 21.02.2017.
 */
class Sms {
    var contact: Contact = Contact()
    var message: String = ""
    var date: Date = Date()
    var type: Int= 0

    constructor()

    constructor(contact: Contact, message: String, date: Date, type: Int) {
        this.contact = contact
        this.message = message
        this.date = date
        this.type = type
    }

    override fun toString(): String {
        return "Sms(contact = $contact, message = $message, date = $date, type = $type)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Sms

        if (contact != other.contact) return false
        if (message != other.message) return false
        if (date != other.date) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contact.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + type
        return result
    }
}