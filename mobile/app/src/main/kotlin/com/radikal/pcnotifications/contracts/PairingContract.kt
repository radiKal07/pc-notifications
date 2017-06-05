package com.radikal.pcnotifications.contracts

import com.radikal.pcnotifications.model.domain.ServerDetails

/**
 * Created by tudor on 17.02.2017.
 */
interface PairingContract {
    interface View : BaseView {
        fun showMessage(message: String)
        fun onServerFound()
        fun onServerFindFailed()
    }

    interface Presenter : BasePresenter<PairingContract.View> {
        fun attachStateListeners()
        fun onServerDetails(serverDetails: ServerDetails)
        fun isConnected(): Boolean
    }
}