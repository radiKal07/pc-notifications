package com.radikal.pcnotifications.presenter

import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.service.NetworkDiscovery
import com.radikal.pcnotifications.model.validators.impl.PortValidator
import javax.inject.Inject

/**
 * Created by tudor on 17.02.2017.
 */
class PairingPresenter @Inject constructor(var networkDiscovery: NetworkDiscovery, var portValidator: PortValidator) : PairingContract.Presenter {
    lateinit private var pairingView: PairingContract.View

    override fun onPortSubmitted(port: String?) {
        if (portValidator.isValid(port)) {
            networkDiscovery.getServerIp(port!!.toInt(), onSuccess = {
                pairingView.showMessage("Connected successfully")
                pairingView.onServerFound()
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