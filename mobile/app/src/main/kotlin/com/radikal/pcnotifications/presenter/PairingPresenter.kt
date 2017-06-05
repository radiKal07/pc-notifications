package com.radikal.pcnotifications.presenter

import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import com.radikal.pcnotifications.model.validators.Validator
import javax.inject.Inject

/**
 * Created by tudor on 17.02.2017.
 */
class PairingPresenter @Inject constructor() : PairingContract.Presenter {

    lateinit private var pairingView: PairingContract.View

    @Inject
    lateinit var deviceCommunicator: DeviceCommunicator

    @Inject
    lateinit var portValidator: Validator<String?>

    override fun attachStateListeners() {
        deviceCommunicator.setErrorListener {
            pairingView.showMessage("Error finding server")
            pairingView.onServerFindFailed()
        }
        deviceCommunicator.setSuccessListener {
            pairingView.onServerFound()
        }
    }

    override fun onServerDetails(serverDetails: ServerDetails) {
        if (portValidator.isValid(serverDetails.port.toString())) {
            deviceCommunicator.connect(serverDetails)
        }
    }

    override fun setView(view: PairingContract.View) {
        this.pairingView = view
    }

    override fun isConnected(): Boolean {
        return deviceCommunicator.isConnected()
    }
}