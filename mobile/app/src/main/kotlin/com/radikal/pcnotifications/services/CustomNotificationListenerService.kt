package com.radikal.pcnotifications.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * Created by tudor on 17.02.2017.
 */
class CustomNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        Log.v("NOTIFICATION", "RECEIVED")
        super.onNotificationPosted(sbn)
        if (sbn == null) {
            return
        }
        Log.v("NOTIFICATION", sbn.notification.extras.toString())
    }
}