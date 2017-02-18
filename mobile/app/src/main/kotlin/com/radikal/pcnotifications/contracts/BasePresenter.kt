package com.radikal.pcnotifications.contracts

/**
 * Created by tudor on 18.02.2017.
 */
interface BasePresenter<in T : BaseView> {
    fun setView(view: T)
}