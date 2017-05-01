package com.radikal.pcnotifications.model.service.impl

import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.persistence.SmsDao
import com.radikal.pcnotifications.model.service.SmsService
import javax.inject.Inject

/**
 * Created by tudor on 5/1/17.
 */
class SmsServiceImpl @Inject constructor() : SmsService {
    @Inject
    lateinit var smsDao: SmsDao

    override fun getAllThreads(): Map<String, List<Sms>> {
        val smsThreadsMap: MutableMap<String, List<Sms>> = HashMap()

        val allReceived = smsDao.getAllReceived()
        val allSent = smsDao.getAllSent()

        val receivedAddresses: List<String> = allReceived.map { it.address }.distinct()
        val sentAddresses: List<String> = allSent.map { it.address }.distinct()

        for (receivedAddress in receivedAddresses) {
            val filterReceived = allReceived.filter { it.address == receivedAddress }
            val filterSent = allSent.filter { it.address == receivedAddress }
            smsThreadsMap.put(receivedAddress, (filterReceived + filterSent).sortedBy { it.date })
        }

        val notResponded = sentAddresses.filter { !receivedAddresses.contains(it) }
        for (notRespondedAddr in notResponded) {
            val filterSent = allSent.filter { it.address == notRespondedAddr }
            smsThreadsMap.put(notRespondedAddr, filterSent.sortedBy { it.date })
        }

        return smsThreadsMap
    }
}