package com.radikal.pcnotifications.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.model.domain.Notification
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.services.util.SmsIdentifier
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject


/**
 * Created by tudor on 17.02.2017.
 */
class CustomNotificationListenerService : NotificationListenerService() {
    val TAG = javaClass.simpleName

    @Inject
    lateinit var smsIdentifier: SmsIdentifier

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        (application as MainApplication).appComponent.inject(this)

        Log.v(TAG, "Received notification")
        super.onNotificationPosted(sbn)
        if (sbn == null) {
            return
        }

        val extras = sbn.notification.extras
        val title = extras.getString("android.title")
        val text = extras.getString("android.text")

        if (StringUtils.isBlank(title) || StringUtils.isBlank(text)) {
            return
        }

        val notification = Notification(title, text)
        Log.v(TAG, notification.toString())
        //TODO send it
        if (smsIdentifier.isSms(applicationContext, sbn)) {
            Log.v(TAG, "notification is SMS")
            // because some SMS app may block the broadcast of SMSs we have to get them manually from the notifications
            val sms = Sms(title, extras.getString("android.bigText") ?: "")
            // TODO send it
        }
    }
}