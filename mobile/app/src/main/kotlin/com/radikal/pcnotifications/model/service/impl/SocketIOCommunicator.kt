package com.radikal.pcnotifications.model.service.impl

import com.radikal.pcnotifications.exceptions.DeviceNotConnectedException
import com.radikal.pcnotifications.model.domain.Notification
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import io.socket.client.IO
import io.socket.client.Socket

/**
 * Created by tudor on 21.02.2017.
 */
class SocketIOCommunicator : DeviceCommunicator {
    private val NOTIFICATION_POSTED_EVENT = "notification_posted"
    private val SMS_POSTED_EVENT = "sms_posted"
    private val SEND_SMS_EVENT = "send_sms"
    private val SERVER_DISCONNECTED = "server_disconnected"
    private val CONNECT_ERROR = "connect_error"
    private var socket: Socket? = null

    override fun connectToServer(ip: String, port: Int) {
        socket = IO.socket("http://$ip:$port")

        socket!!.on(CONNECT_ERROR) {
            socket!!.close()
            throw DeviceNotConnectedException("Failed to initialize connection")
        }
        // get ip and port form persistence and create the socket
        // add send sms listener event on the socket
        // if no ip or port then throw
    }

    override fun postNotification(notification: Notification) {
        // TODO check if the socket is initialized
        // if it is then send the notification
        // else throw
    }

    override fun postSms(sms: Sms) {
        // TODO check if the socket is initialized
        // if it is then send the sms
        // else throw
    }
}