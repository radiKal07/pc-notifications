package com.radikal.pcnotifications.services

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.exceptions.ServerDetailsNotFoundException
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import java.net.DatagramPacket
import java.net.DatagramSocket
import javax.inject.Inject


/**
 * Created by tudor on 05.03.2017.
 */
class ServerWakeListener : IntentService("ServerWakeListener") {
    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

    override fun onHandleIntent(intent: Intent?) {
        startListener()
    }

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG, "UDP Listener onBind")
        return null
    }

    override fun onCreate() {
        Log.v(TAG, "UDP Listener onCreate")
        (this.application as MainApplication).appComponent.inject(this)
        super.onCreate()
    }

    fun startListener() {
        Log.v(TAG, "UDP Listener started")
        val serverDetails: ServerDetails
        try {
            serverDetails = serverDetailsDao.retrieve()
        } catch (e: ServerDetailsNotFoundException) {
            Log.e(TAG, "Can't find port", e)
            return
        }

        val port = serverDetails.port
        val serverSocket = DatagramSocket(port)
        val receiveData = ByteArray(8)


        val receivePacket = DatagramPacket(receiveData,
                receiveData.size)

        while (true) {
            Log.v(TAG, "Listening...")
            serverSocket.receive(receivePacket)
            val sentence = String(receivePacket.data, 0,
                    receivePacket.length)
            Log.v(TAG, "UDP received: $sentence")
            if ("wake" == sentence) {
                deviceCommunicator.connect()
            }
            // now send acknowledgement packet back to sender
            val IPAddress = receivePacket.address
            val sendString = "connecting"
            val sendData = sendString.toByteArray(charset("UTF-8"))
            val sendPacket = DatagramPacket(sendData, sendData.size,
                    IPAddress, receivePacket.port)
            serverSocket.send(sendPacket)
        }
    }
}