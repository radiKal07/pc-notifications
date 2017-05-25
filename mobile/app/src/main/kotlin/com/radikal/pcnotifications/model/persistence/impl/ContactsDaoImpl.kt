package com.radikal.pcnotifications.model.persistence.impl

import com.github.tamir7.contacts.Contacts
import com.radikal.pcnotifications.model.domain.Contact
import com.radikal.pcnotifications.model.persistence.ContactsDao

/**
 * Created by tudor on 5/23/17.
 */
class ContactsDaoImpl : ContactsDao {
    override fun getAll(): List<Contact> {
        val contacts: MutableList<Contact> = ArrayList()
        for (contact in Contacts.getQuery().find()) {
            val phoneNumbers = contact.phoneNumbers
            if (phoneNumbers.size > 0) {
                val firstNumber = phoneNumbers.iterator().next()
                var phoneNumber = firstNumber.number.replace(" ", "")
                if (phoneNumber.isEmpty()) {
                    phoneNumber = firstNumber.normalizedNumber.replace(" ", "")
                }
                if (phoneNumber.isEmpty() || contact.displayName.isEmpty()) {
                    continue
                }
                val element = Contact(contact.displayName, phoneNumber)
                if (!contacts.contains(element)) {
                    // do not add duplicates
                    contacts.add(element)
                }
            }
        }
        return contacts
    }
}