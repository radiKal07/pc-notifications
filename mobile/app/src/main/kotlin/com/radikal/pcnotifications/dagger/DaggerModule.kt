package com.radikal.pcnotifications.dagger

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.service.NetworkDiscovery
import com.radikal.pcnotifications.model.validators.Validator
import com.radikal.pcnotifications.model.validators.impl.PortValidator
import com.radikal.pcnotifications.presenter.PairingPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named
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

    @Provides
    @Singleton
    fun networkDiscovery(wifiManager: WifiManager): NetworkDiscovery {
        return NetworkDiscovery(wifiManager)
    }

    @Provides
    @Singleton
    @Named("portValidator")
    fun portValidator(): PortValidator {
        return PortValidator()
    }

    @Provides
    @Singleton
    fun pairingPresenter(networkDiscovery: NetworkDiscovery, portValidator: PortValidator): PairingContract.Presenter {
        return PairingPresenter(networkDiscovery, portValidator)
    }
}