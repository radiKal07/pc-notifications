package com.radikal.pcnotifications.dagger

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.persistence.SmsDao
import com.radikal.pcnotifications.model.persistence.impl.SqliteSmsDao
import com.radikal.pcnotifications.model.service.*
import com.radikal.pcnotifications.model.service.impl.*
import com.radikal.pcnotifications.model.validators.Validator
import com.radikal.pcnotifications.model.validators.impl.PortValidator
import com.radikal.pcnotifications.presenter.PairingPresenter
import com.radikal.pcnotifications.listeners.util.SmsIdentifier
import com.radikal.pcnotifications.model.persistence.ContactsDao
import com.radikal.pcnotifications.model.persistence.impl.ContactsDaoImpl
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
    @Named("portValidator")
    fun portValidator(): Validator<String?> {
        return PortValidator()
    }

    @Provides
    @Singleton
    fun pairingPresenter(deviceCommunicator: DeviceCommunicator): PairingContract.Presenter {
        val pairingPresenter = PairingPresenter()
        pairingPresenter.portValidator = portValidator()
        pairingPresenter.deviceCommunicator = deviceCommunicator
        return pairingPresenter
    }

    @Provides
    @Singleton
    fun deviceCommunicator(wifiManager: WifiManager, serverDetailsDao: ServerDetailsDao, dataSerializer: DataSerializer, smsService: SmsService, contactsDao: ContactsDao): DeviceCommunicator {
        val socketIOCommunicator = SocketIOCommunicator()
        socketIOCommunicator.serverDetailsDao = serverDetailsDao
        socketIOCommunicator.dataSerializer = dataSerializer
        socketIOCommunicator.smsService = smsService
        socketIOCommunicator.wifiManager = wifiManager
        socketIOCommunicator.contactsDao = contactsDao
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

    @Provides
    @Singleton
    fun contentResolver(): ContentResolver {
        return application.contentResolver
    }

    @Provides
    @Singleton
    fun smsDao(contentResolver: ContentResolver, telephonyManager: TelephonyManager): SmsDao {
        val sqliteSmsDao = SqliteSmsDao()
        sqliteSmsDao.contentResolver = contentResolver
        sqliteSmsDao.telephonyManager = telephonyManager
        return sqliteSmsDao
    }

    @Provides
    @Singleton
    fun smsService(smsDao: SmsDao, application: Application): SmsService {
        val smsService = SmsServiceImpl()
        smsService.smsDao = smsDao
        smsService.context = application
        return smsService
    }

    @Provides
    @Singleton
    fun telephonyManager(): TelephonyManager {
        return application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    @Singleton
    fun contactsDao(): ContactsDao {
        return ContactsDaoImpl()
    }
}