package com.radikal.pcnotifications.model.service

import com.radikal.pcnotifications.model.domain.Sms

/**
 * Created by tudor on 5/1/17.
 */
interface SmsService {
    fun getAllThreads(): Map<String, List<Sms>>
}