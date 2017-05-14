package com.radikal.pcnotifications.listeners

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import javax.inject.Inject

/**
 * Created by tudor on 5/14/17.
 */
class SentSmsStatusListener : BroadcastReceiver() {
    val TAG: String = javaClass.simpleName

    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Sms sent status: $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            return
        } else {
            Log.e(TAG, "Error sending sms. Result code = $resultCode")
            // TODO notify client of error
        }
    }
}