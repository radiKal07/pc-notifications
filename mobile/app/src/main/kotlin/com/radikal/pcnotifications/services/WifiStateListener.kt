package com.radikal.pcnotifications.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.radikal.pcnotifications.MainApplication
import javax.inject.Inject

/**
 * Created by tudor on 26.02.2017.
 */
class WifiStateListener : BroadcastReceiver() {
    val TAG = javaClass.simpleName
    @Inject
    lateinit var wifiManager: WifiManager

    override fun onReceive(context: Context?, intent: Intent?) {
        (context!!.applicationContext as MainApplication).appComponent.inject(this)
        if (wifiManager.isWifiEnabled) {
            Log.v(TAG, "Wi-Fi enabled")
            // TODO connect to the server
        } else {
            Log.v(TAG, "Wi-Fi disabled")
            // TODO disconnect from the server
        }
    }
}