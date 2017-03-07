package com.radikal.pcnotifications.model.service.impl

import android.util.Log
import com.radikal.pcnotifications.exceptions.DeviceNotConnectedException
import com.radikal.pcnotifications.exceptions.ServerDetailsNotFoundException
import com.radikal.pcnotifications.model.domain.Notification
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.service.DataSerializer
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Inject

/**
 * Created by tudor on 21.02.2017.
 */
class SocketIOCommunicator @Inject constructor() : DeviceCommunicator {
    private val TAG = javaClass.simpleName

    private val NOTIFICATION_POSTED_EVENT = "notification_posted"
    private val SMS_POSTED_EVENT = "sms_posted"
    private val SEND_SMS_EVENT = "send_sms"
    private val SERVER_DISCONNECTED = "server_disconnected"
    private val CONNECT_ERROR = "connect_error"
    private val CONNECT = "connect"

    private var socket: Socket? = null

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    @Inject
    lateinit var dataSerializer: DataSerializer

    override fun connect() {
        if (isConnected()) {
            return
        }

        try {
            val (hostname, ip, port) = serverDetailsDao.retrieve()
            socket = IO.socket("http://$ip:$port")
            Log.v(TAG, "Trying to connect to http://$ip:$port ($hostname)")
        } catch (e: ServerDetailsNotFoundException) {
            Log.e(TAG, "Failed to connect", e)
            throw DeviceNotConnectedException("Failed to connect to device", e)
        }

        socket!!.on(CONNECT_ERROR) {
            Log.v(TAG, CONNECT_ERROR)
            if (it.isNotEmpty()) {
                for (el in it) {
                    Log.v(TAG, el.toString())
                }
            }
            socket!!.close()
            socket = null
        }

        socket!!.on(SEND_SMS_EVENT) {
            Log.v(TAG, SEND_SMS_EVENT)
            val sms = dataSerializer.deserialize(it.toString(), Sms::class.java)
            // TODO send sms to contact
            // "it" should contain the message and the contact
        }

        socket!!.on(SERVER_DISCONNECTED) {
            Log.v(TAG, SERVER_DISCONNECTED)
            socket!!.close()
        }

        socket!!.on(CONNECT) {
            Log.v(TAG, "Connected successfully")
        }

        socket!!.connect()
    }

    override fun disconnect() {
        Log.v(TAG, "disconnecting")
        socket?.close()
    }

    override fun postNotification(notification: Notification) {
        Log.v(TAG, "Posting notification")
        if (!isConnected()) {
            Log.v(TAG, "Socket not connected")
            return
        }
        socket?.emit(NOTIFICATION_POSTED_EVENT, dataSerializer.serialize(notification))
    }

    override fun postSms(sms: Sms) {
        Log.v(TAG, "Posting SMS")
        if (!isConnected()) {
            Log.v(TAG, "Socket not connected")
            return
        }
        socket?.emit(SMS_POSTED_EVENT, dataSerializer.serialize(sms))
    }

    override fun isConnected(): Boolean {
        if (socket == null) {
            return false
        }
        return socket!!.connected()
    }

}