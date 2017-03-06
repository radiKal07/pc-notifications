package com.radikal.pcnotifications.presenter

import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.service.NetworkDiscovery
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import com.radikal.pcnotifications.model.validators.Validator
import javax.inject.Inject

/**
 * Created by tudor on 17.02.2017.
 */
class PairingPresenter @Inject constructor() : PairingContract.Presenter {
    lateinit private var pairingView: PairingContract.View

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery

    @Inject
    lateinit var portValidator: Validator<String?>

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    override fun onPortSubmitted(port: String?) {
        if (portValidator.isValid(port)) {
            networkDiscovery.getServerIp(port!!.toInt(), onSuccess = { address: String, hostname: String ->
                // TODO save server Ip
                pairingView.showMessage("Connected successfully")
                val serverDetails = ServerDetails(hostname, address, port.toInt())
                serverDetailsDao.save(serverDetails)
                pairingView.onServerFound(serverDetails)
            }, onError = {
                pairingView.showMessage(it.message ?: "Failed to find device")
                pairingView.onServerFindFailed()
            })
        } else {
            pairingView.showMessage("Invalid pairing code provided")
            pairingView.onServerFindFailed()
        }
    }

    override fun setView(view: PairingContract.View) {
        this.pairingView = view
    }
}