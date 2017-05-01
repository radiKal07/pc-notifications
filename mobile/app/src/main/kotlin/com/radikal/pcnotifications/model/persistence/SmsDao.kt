package com.radikal.pcnotifications.model.persistence

import com.radikal.pcnotifications.model.domain.Sms

/**
 * Created by tudor on 25.04.2017.
 */
interface SmsDao {
    fun getAllReceived(): List<Sms>
    fun getAllSent(): List<Sms>
}