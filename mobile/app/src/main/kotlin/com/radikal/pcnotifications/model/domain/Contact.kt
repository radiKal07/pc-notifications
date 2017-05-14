package com.radikal.pcnotifications.model.domain

/**
 * Created by tudor on 5/14/17.
 */
class Contact {
    var displayName: String = ""
    var phoneNumber: String = ""

    constructor()

    constructor(displayName: String, phoneNumber: String) {
        this.displayName = displayName
        this.phoneNumber = phoneNumber
    }

    override fun toString(): String {
        return "Contact(displayName = $displayName, phoneNumber = $phoneNumber)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Contact

        if (displayName != other.displayName) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = displayName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }
}