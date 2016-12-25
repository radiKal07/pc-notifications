package com.radikal.pcnotifications

import android.app.Application
import com.radikal.pcnotifications.dagger.AppComponent
import com.radikal.pcnotifications.dagger.DaggerAppComponent
import com.radikal.pcnotifications.dagger.DaggerModule

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
    }
}