package com.radikal.pcnotifications.model.service

/**
 * Created by tudor on 21.02.2017.
 */
interface NetworkDiscovery {
    fun getServerIp(port: Int, onSuccess: (String, String) -> Unit, onError: (Exception) -> Unit)
}