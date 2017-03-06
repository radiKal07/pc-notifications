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
    val SERVER_HOSTNAME = "server_hostname"
    val SERVER_IP = "server_ip"
    val SERVER_PORT = "server_port"

    override fun save(serverDetails: ServerDetails) {
        val editor = sharedPreferences.edit()
        editor.putString(SERVER_HOSTNAME, serverDetails.hostname)
        editor.putString(SERVER_IP, serverDetails.ip)
        editor.putInt(SERVER_PORT, serverDetails.port)
        editor.apply()
    }

    override fun retrieve(): ServerDetails {
        val hostname = sharedPreferences.getString(SERVER_HOSTNAME, "Default")
        val ip = sharedPreferences.getString(SERVER_IP, null)
        val port = sharedPreferences.getInt(SERVER_PORT, -1)

        if (ip == null || port == -1) {
            throw ServerDetailsNotFoundException("Server details not found")
        }

        return ServerDetails(hostname, ip, port)
    }

    override fun delete() {
        val editor = sharedPreferences.edit()
        editor.remove(SERVER_HOSTNAME)
        editor.remove(SERVER_IP)
        editor.remove(SERVER_PORT)
        editor.apply()
    }
}