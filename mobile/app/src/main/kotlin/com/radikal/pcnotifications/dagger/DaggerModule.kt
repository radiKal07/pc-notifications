package com.radikal.pcnotifications.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.preference.PreferenceManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.service.DataSerializer
import com.radikal.pcnotifications.model.service.DeviceCommunicator
import com.radikal.pcnotifications.model.service.NetworkDiscovery
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import com.radikal.pcnotifications.model.service.impl.JSONDataSerializer
import com.radikal.pcnotifications.model.service.impl.SharedPreferencesServerDetailsDao
import com.radikal.pcnotifications.model.service.impl.SocketIOCommunicator
import com.radikal.pcnotifications.model.service.impl.SocketIONetworkDiscovery
import com.radikal.pcnotifications.model.validators.Validator
import com.radikal.pcnotifications.model.validators.impl.PortValidator
import com.radikal.pcnotifications.presenter.PairingPresenter
import com.radikal.pcnotifications.services.util.SmsIdentifier
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
    fun sharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    }

    @Provides
    @Singleton
    fun wifiManager(): WifiManager {
        return application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @Provides
    @Singleton
    @Named("socketIONetworkDiscovery")
    fun networkDiscovery(wifiManager: WifiManager): NetworkDiscovery {
        return SocketIONetworkDiscovery(wifiManager)
    }

    @Provides
    @Singleton
    @Named("portValidator")
    fun portValidator(): Validator<String?> {
        return PortValidator()
    }

    @Provides
    @Singleton
    fun pairingPresenter(@Named("socketIONetworkDiscovery") networkDiscovery: NetworkDiscovery, serverDetailsDao: ServerDetailsDao): PairingContract.Presenter {
        val pairingPresenter = PairingPresenter()
        pairingPresenter.networkDiscovery = networkDiscovery
        pairingPresenter.portValidator = portValidator()
        pairingPresenter.serverDetailsDao = serverDetailsDao
        return pairingPresenter
    }

    @Provides
    @Singleton
    fun deviceCommunicator(serverDetailsDao: ServerDetailsDao, dataSerializer: DataSerializer): DeviceCommunicator {
        val socketIOCommunicator = SocketIOCommunicator()
        socketIOCommunicator.serverDetailsDao = serverDetailsDao
        socketIOCommunicator.dataSerializer = dataSerializer
        return socketIOCommunicator
    }

    @Provides
    @Singleton
    fun smsIdentifier(): SmsIdentifier {
        return SmsIdentifier()
    }

    @Provides
    @Singleton
    fun serverDetailsDao(sharedPreferences: SharedPreferences): ServerDetailsDao {
        return SharedPreferencesServerDetailsDao(sharedPreferences)
    }

    @Provides
    @Singleton
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    @Provides
    @Singleton
    fun dataSerializer(objectMapper: ObjectMapper): DataSerializer {
        return JSONDataSerializer(objectMapper)
    }
}