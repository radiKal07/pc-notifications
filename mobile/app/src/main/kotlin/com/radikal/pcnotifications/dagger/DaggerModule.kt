package com.radikal.pcnotifications.dagger

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by tudor on 14.12.2016.
 */
@Module
class DaggerModule(val application: Application) {

    @Provides
    @Singleton
    fun application(): Application {
        return application
    }

    @Provides
    @Singleton
    fun wifiManager(): WifiManager {
        return application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

}