package com.radikal.pcnotifications.model.persistence

import com.radikal.pcnotifications.model.domain.Contact

/**
 * Created by tudor on 5/23/17.
 */
interface ContactsDao {
    fun getAll(): List<Contact>
}