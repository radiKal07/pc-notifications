package com.radikal.pcnotifications.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.provider.Telephony
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.model.domain.Contact
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import java.util.*
import javax.inject.Inject


/**
 * Created by tudor on 5/14/17.
 */
class SmsListener : BroadcastReceiver() {
    val TAG: String = javaClass.simpleName

    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

    override fun onReceive(context: Context?, intent: Intent?) {
        (context!!.applicationContext as MainApplication).appComponent.inject(this)

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action) {
            Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    .map { Sms(Contact(it.originatingAddress, it.serviceCenterAddress), it.messageBody, Date(), 1) }
                    .forEach {
                        Log.d(TAG, "Received sms: $it")
                        deviceCommunicator.postSms(it)
                    }
        }
    }
}