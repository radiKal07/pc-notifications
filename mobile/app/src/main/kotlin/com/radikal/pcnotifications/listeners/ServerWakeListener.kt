package com.radikal.pcnotifications.listeners

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.exceptions.DeviceNotConnectedException
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
class ServerWakeListener : Service() {
    private val TAG = javaClass.simpleName

    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        StartListenerAsyncTask(serverDetailsDao, deviceCommunicator).execute()
        return START_STICKY
    }

    class StartListenerAsyncTask(serverDetailsDao: ServerDetailsDao, deviceCommunicator: DeviceCommunicator) : AsyncTask<Void, Void, Void>() {
        private val TAG = javaClass.simpleName

        var serverDetailsDao: ServerDetailsDao? = serverDetailsDao
        var deviceCommunicator: DeviceCommunicator? = deviceCommunicator

        override fun doInBackground(vararg params: Void?): Void? {
            startListener()
            return null
        }

        private fun startListener() {
            Log.v(TAG, "UDP Listener started")
            val serverDetails: ServerDetails
            try {
                serverDetails = serverDetailsDao!!.retrieve()
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
                    try {
                        deviceCommunicator!!.connect()
                    } catch (exception: DeviceNotConnectedException) {
                        Log.e(TAG, exception.message, exception)
                    }

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
}