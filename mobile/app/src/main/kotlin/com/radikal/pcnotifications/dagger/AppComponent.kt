package com.radikal.pcnotifications.dagger

import com.radikal.pcnotifications.activities.MainActivity
import com.radikal.pcnotifications.fragments.PairingFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by tudor on 14.12.2016.
 */
@Singleton
@Component(modules = arrayOf(DaggerModule::class))
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(pairingFragment: PairingFragment)
}