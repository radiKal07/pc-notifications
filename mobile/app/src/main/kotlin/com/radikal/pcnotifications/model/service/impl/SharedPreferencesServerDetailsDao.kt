package com.radikal.pcnotifications.model.service.impl

import android.content.SharedPreferences
import com.radikal.pcnotifications.exceptions.ServerDetailsNotFoundException
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import javax.inject.Inject

/**
 * Created by tudor on 26.02.2017.
 */
class SharedPreferencesServerDetailsDao @Inject constructor(var sharedPreferences: SharedPreferences) : ServerDetailsDao{
    val SERVER_IP = "server_ip"
    val SERVER_PORT = "server_port"

    override fun save(serverDetails: ServerDetails) {
        val editor = sharedPreferences.edit()
        editor.putString(SERVER_IP, serverDetails.ip)
        editor.putInt(SERVER_PORT, serverDetails.port)
        editor.apply()
    }

    override fun retrieve(): ServerDetails {
        val ip = sharedPreferences.getString(SERVER_IP, null)
        val port = sharedPreferences.getInt(SERVER_IP, -1)

        if (ip == null || port == -1) {
            throw ServerDetailsNotFoundException("Server details not found")
        }

        return ServerDetails(ip, port)
    }
}