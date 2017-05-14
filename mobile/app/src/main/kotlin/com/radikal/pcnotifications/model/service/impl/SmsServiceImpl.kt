package com.radikal.pcnotifications.model.service.impl

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.radikal.pcnotifications.model.domain.Contact
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.persistence.SmsDao
import com.radikal.pcnotifications.model.service.SmsService
import javax.inject.Inject

/**
 * Created by tudor on 5/1/17.
 */
class SmsServiceImpl @Inject constructor() : SmsService {
    val TAG: String = javaClass.simpleName

    @Inject
    lateinit var smsDao: SmsDao

    @Inject
    lateinit var context: Application

    override fun getAllThreads(): Map<String, List<Sms>> {
        val smsThreadsMap: MutableMap<String, List<Sms>> = HashMap()

        val allReceived = smsDao.getAllReceived()
        val allSent = smsDao.getAllSent()

        val receivedAddresses: List<String> = allReceived.map { it.contact.displayName }.distinct()
        val sentAddresses: List<String> = allSent.map { it.contact.displayName }.distinct()

        for (receivedAddress in receivedAddresses) {
            val filterReceived = allReceived.filter { it.contact.displayName == receivedAddress }
            val filterSent = allSent.filter { it.contact.displayName == receivedAddress }
            smsThreadsMap.put(receivedAddress, (filterReceived + filterSent).sortedBy { it.date })
        }

        val notResponded = sentAddresses.filter { !receivedAddresses.contains(it) }
        for (notRespondedAddr in notResponded) {
            val filterSent = allSent.filter { it.contact.displayName == notRespondedAddr }
            smsThreadsMap.put(notRespondedAddr, filterSent.sortedBy { it.date })
        }

        return smsThreadsMap
    }

    override fun sendSms(sms: Sms) {
        Log.v(TAG, sms.toString())
        val sentPi = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        //SmsManager.getDefault().sendTextMessage(sms.contact.phoneNumber, null, sms.message, sentPi, null)
    }
}