package com.radikal.pcnotifications.contracts

/**
 * Created by tudor on 17.02.2017.
 */
interface PairingContract {
    interface View : BaseView {
        fun showMessage(message: String)
        fun onServerFound(ip: String, port: Int)
        fun onServerFindFailed()
    }

    interface Presenter : BasePresenter<PairingContract.View> {
        fun onPortSubmitted(port: String?)
    }
}