package com.radikal.pcnotifications.services.util

import android.content.Context
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * Created by tudor on 21.02.2017.
 */
class SmsIdentifier {
    val TAG = javaClass.simpleName

    fun isSms(context: Context, sbn: StatusBarNotification): Boolean {
        val smsApp = Settings.Secure.getString(context.contentResolver, "sms_default_application")
        if (smsApp.contains(sbn.packageName)) {
            Log.v(TAG, "SMS received")
            return true
        }
        return false
    }
}