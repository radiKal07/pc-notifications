package com.radikal.pcnotifications.model.service

import com.radikal.pcnotifications.model.domain.Sms

/**
 * Created by tudor on 25.04.2017.
 */
interface SmsDao {
    fun getAll(): List<Sms>
}