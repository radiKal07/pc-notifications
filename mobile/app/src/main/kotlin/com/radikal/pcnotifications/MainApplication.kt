package com.radikal.pcnotifications

import android.app.Application
import android.content.Intent
import com.github.tamir7.contacts.Contacts
import com.radikal.pcnotifications.dagger.AppComponent
import com.radikal.pcnotifications.dagger.DaggerAppComponent
import com.radikal.pcnotifications.dagger.DaggerModule
import com.radikal.pcnotifications.listeners.ServerWakeListener

/**
 * Created by tudor on 14.12.2016.
 */
class MainApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .daggerModule(DaggerModule(this))
                .build()
        Contacts.initialize(this)

        val serverWakeIntent = Intent(this, ServerWakeListener::class.java)
        startService(serverWakeIntent)
    }
}