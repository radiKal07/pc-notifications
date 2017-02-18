package com.radikal.pcnotifications.dagger

import com.radikal.pcnotifications.view.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by tudor on 14.12.2016.
 */
@Singleton
@Component(modules = arrayOf(DaggerModule::class))
interface AppComponent {
    fun inject(activity: MainActivity)
}