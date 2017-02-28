package com.radikal.pcnotifications.model.service.impl

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
    private val NOTIFICATION_POSTED_EVENT = "notification_posted"
    private val SMS_POSTED_EVENT = "sms_posted"
    private val SEND_SMS_EVENT = "send_sms"
    private val SERVER_DISCONNECTED = "server_disconnected"
    private val CONNECT_ERROR = "connect_error"
    private var socket: Socket? = null

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    @Inject
    lateinit var dataSerializer: DataSerializer

    override fun connect() {
        try {
            val (ip, port) = serverDetailsDao.retrieve()
            socket = IO.socket("http://$ip:$port")
        } catch (e: ServerDetailsNotFoundException) {
            throw DeviceNotConnectedException("Failed to connect to device", e)
        }

        socket!!.on(CONNECT_ERROR) {
            socket!!.close()
        }

        socket!!.on(SEND_SMS_EVENT) {
            val sms = dataSerializer.deserialize(it.toString(), Sms::class.java)
            // TODO send sms to contact
            // "it" should contain the message and the contact
        }

        socket!!.on(SERVER_DISCONNECTED) {
            socket!!.close()
        }
    }

    override fun disconnect() {
        socket?.close()
    }

    override fun postNotification(notification: Notification) {
        socket?.emit(NOTIFICATION_POSTED_EVENT, dataSerializer.serialize(notification))
    }

    override fun postSms(sms: Sms) {
        socket?.emit(SMS_POSTED_EVENT, dataSerializer.serialize(sms))
    }
}