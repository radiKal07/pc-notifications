package com.radikal.pcnotifications.model.service.impl

import android.net.wifi.WifiManager
import android.text.format.Formatter
import com.radikal.pcnotifications.model.service.NetworkDiscovery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.socket.client.Ack
import io.socket.client.IO
import org.apache.commons.net.util.SubnetUtils
import java.net.NetworkInterface
import java.net.SocketException
import javax.inject.Inject


/**
 * Created by tudor on 14.12.2016.
 */
class SocketIONetworkDiscovery @Inject constructor(var wifiManager: WifiManager) : NetworkDiscovery {

    override fun getServerIp(port: Int, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        if (!wifiManager.isWifiEnabled) {
            onError(IllegalStateException("Please enable Wi-Fi"))
            return
        }

        val wifiInterface: NetworkInterface = getWiFiNetworkInterface()
        // due to Android bug (netmask is always zero) we have to find this value manually
        val netmask = getNetmask(wifiInterface)
        val allAddresses = getAddressesInRange(wifiManager.dhcpInfo.gateway, netmask)

        var failedAttempts = 0
        var serverFound = false
        var index = 0

        while (index < allAddresses.size && !serverFound) {
            val address = allAddresses[index++]
            Observable.just(address)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        val socket = IO.socket("http://$address:$port")
                        socket.on("connect_error") {
                            socket.close()
                            socket.disconnect()
                            failedAttempts++
                            if (failedAttempts == allAddresses.size) {
                                onError(IllegalStateException("Failed to find device"))
                            }
                        }
                        socket.connect()
                        socket.emit("trying", "knock knock", Ack {
                            if (socket.connected()) {
                                serverFound = true
                                failedAttempts = -allAddresses.size
                                onSuccess(address)
                            }
                        })
                    }
        }
    }

    /**
     * Get the WiFi network interface
     */
    private fun getWiFiNetworkInterface(): NetworkInterface {
        var res: NetworkInterface? = null
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val reverseIpAddress = Integer.reverseBytes(ipAddress)

        var found = false
        for (networkInterface in networkInterfaces) {
            if (found) {
                break
            }
            val inetAddresses = networkInterface.inetAddresses
            for (inetAddress in inetAddresses) {
                val byteArrayToInt = byteArrayToInt(inetAddress.address, 0)
                if (byteArrayToInt == ipAddress || byteArrayToInt == reverseIpAddress) {
                    res = networkInterface
                    found = true
                    break
                }
            }
        }

        if (res == null) {
            throw SocketException("Could not retrieve server IP")
        }
        return res
    }

    /**
     * For the given NetworkInterface get the netmask
     * If the netmask is bigger than 32 then return 32
     */
    private fun getNetmask(networkInterface: NetworkInterface): Short {
        val netmask: Short = networkInterface.interfaceAddresses.minBy { it.networkPrefixLength }?.networkPrefixLength ?: return 32
        if (netmask > 32) return 32 else return netmask
    }

    /**
     * Get the range of IPs for the given IP with the corresponding netmask
     */
    private fun getAddressesInRange(ip: Int, netmask: Short): Array<out String> {
        val ipString = Formatter.formatIpAddress(ip)
        val utils = SubnetUtils("$ipString/$netmask")
        utils.isInclusiveHostCount = true
        val allAddresses = utils.info.allAddresses
        return allAddresses
    }

    /**
     * Convert the given byte array to int
     */
    private fun byteArrayToInt(arr: ByteArray, offset: Int): Int {
        if (arr.size - offset < 4) {
            return -1
        }

        val r0 = (arr[offset].toInt() and 0xFF) shl 24
        val r1 = (arr[offset + 1].toInt() and 0xFF) shl 16
        val r2 = (arr[offset + 2].toInt() and 0xFF) shl 8
        val r3 = arr[offset + 3].toInt() and 0xFF
        return r0 + r1 + r2 + r3
    }
}