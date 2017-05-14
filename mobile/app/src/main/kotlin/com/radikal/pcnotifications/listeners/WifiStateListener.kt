package com.radikal.pcnotifications.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.exceptions.DeviceNotConnectedException
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import javax.inject.Inject

/**
 * Created by tudor on 26.02.2017.
 */
class WifiStateListener : BroadcastReceiver() {
    val TAG: String = javaClass.simpleName

    @Inject
    lateinit var wifiManager: WifiManager

    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

    override fun onReceive(context: Context?, intent: Intent?) {
        (context!!.applicationContext as MainApplication).appComponent.inject(this)
        if (wifiManager.isWifiEnabled) {
            Log.v(TAG, "Wi-Fi enabled")
            try {
                Thread.sleep(5000)
                deviceCommunicator.connect()
            } catch (e: DeviceNotConnectedException) {
                Log.e(TAG, "Failed to connect", e)
            }
        } else {
            Log.v(TAG, "Wi-Fi disabled")
        }
    }
}