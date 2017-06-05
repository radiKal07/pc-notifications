package com.radikal.pcnotifications.model.service.impl

import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import com.radikal.pcnotifications.exceptions.DeviceNotConnectedException
import com.radikal.pcnotifications.exceptions.ServerDetailsNotFoundException
import com.radikal.pcnotifications.model.domain.Notification
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.persistence.ContactsDao
import com.radikal.pcnotifications.model.service.DataSerializer
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import com.radikal.pcnotifications.model.service.SmsService
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Inject

/**
 * Created by tudor on 21.02.2017.
 */
class SocketIOCommunicator @Inject constructor() : DeviceCommunicator {
    private val TAG = javaClass.simpleName

    private val NOTIFICATION_POSTED_EVENT = "notification_posted"
    private val NEW_SMS_EVENT = "new_sms"
    private val SEND_SMS_EVENT = "send_sms"
    private val GET_ALL_SMS_EVENT = "get_all_sms"
    private val GET_ALL_CONTACTS_EVENT = "get_all_contacts"
    private val SERVER_DISCONNECTED = "server_disconnected"
    private val CONNECT_ERROR = "connect_error"
    private val CONNECT = "connect"

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    @Inject
    lateinit var dataSerializer: DataSerializer

    @Inject
    lateinit var smsService: SmsService

    @Inject
    lateinit var wifiManager: WifiManager

    @Inject
    lateinit var contactsDao: ContactsDao

    private var socket: Socket? = null
    private var errorListener: (() -> Unit)? = null
    private var successListener: (() -> Unit)? = null

    override fun connect(serverDetails: ServerDetails) {
        if (isConnected()) {
            return
        }

        serverDetailsDao.save(serverDetails)

        try {
            val (hostname, ip, port) = serverDetails
            socket = IO.socket("http://$ip:$port")
            IO.setDefaultHostnameVerifier { hostname, session ->
                return@setDefaultHostnameVerifier true
            }
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = false
            Log.v(TAG, "Trying to connect to http://$ip:$port ($hostname)")
        } catch (e: ServerDetailsNotFoundException) {
            Log.e(TAG, "Failed to connect", e)
            throw DeviceNotConnectedException("Failed to connect to device", e)
        }

        addSocketEvents()

        val clientIp: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

        socket!!.connect()
        socket!!.emit("server_discovery", clientIp, Ack {
            if (socket!!.connected()) {
                Log.v(TAG, "server_discovery succeeded")
            }
        })
    }

    override fun connect() {
        if (isConnected()) {
            return
        }

        try {
            val (hostname, ip, port) = serverDetailsDao.retrieve()
            socket = IO.socket("http://$ip:$port")
            IO.setDefaultHostnameVerifier { hostname, session ->
                return@setDefaultHostnameVerifier true
            }
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = false
            Log.v(TAG, "Trying to connect to http://$ip:$port ($hostname)")
        } catch (e: ServerDetailsNotFoundException) {
            Log.e(TAG, "Failed to connect", e)
            throw DeviceNotConnectedException("Failed to connect to device", e)
        }

        addSocketEvents()

        socket!!.connect()
    }

    private fun addSocketEvents() {
        socket!!.on(CONNECT_ERROR) {
            Log.v(TAG, CONNECT_ERROR)
            if (it.isNotEmpty()) {
                for (el in it) {
                    Log.v(TAG, el.toString())
                }
            }
            socket?.close()
            socket = null
            errorListener?.invoke()
        }

        socket!!.on(SEND_SMS_EVENT) {
            Log.v(TAG, SEND_SMS_EVENT)
            if (it.size >= 2 && it[0] is String && it[1] is Ack) {
                val smsJson = it[0]
                val ackResp = it[1] as Ack
                val sms = dataSerializer.deserialize(smsJson.toString(), Sms::class.java)
                smsService.sendSms(sms)
                ackResp.call("OK")
            }
        }

        socket!!.on(GET_ALL_SMS_EVENT) {
            Log.v(TAG, GET_ALL_SMS_EVENT)
            if (it[0] is Ack) {
                (it[0] as Ack).call(dataSerializer.serialize(smsService.getAllThreads()))
            }
        }

        socket!!.on(GET_ALL_CONTACTS_EVENT) {
            Log.v(TAG, GET_ALL_CONTACTS_EVENT)
            if (it[0] is Ack) {
                val serialize = dataSerializer.serialize(contactsDao.getAll())
                Log.v(TAG, "Sending contacts: $serialize")
                (it[0] as Ack).call(serialize)
            }
        }

        socket!!.on(SERVER_DISCONNECTED) {
            Log.v(TAG, SERVER_DISCONNECTED)
            socket!!.close()
        }

        socket!!.on(CONNECT) {
            Log.v(TAG, "Connected successfully")
            successListener?.invoke()
        }
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
        socket?.emit(NEW_SMS_EVENT, dataSerializer.serialize(sms))
    }

    override fun isConnected(): Boolean {
        if (socket == null) {
            return false
        }
        val connected = socket!!.connected()
        if (connected) {
            Log.v(TAG, "Socket already connected")
        }

        return connected
    }

    override fun setErrorListener(errorListener: () -> Unit) {
        this.errorListener = errorListener
    }

    override fun setSuccessListener(successListener: () -> Unit) {
        this.successListener = successListener
    }
}