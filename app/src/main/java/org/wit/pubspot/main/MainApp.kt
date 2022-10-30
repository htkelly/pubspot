package org.wit.pubspot.main

import android.app.Application
import org.wit.pubspot.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var unifiedStorage: UnifiedStore
    var loggedInUser : UserModel? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        unifiedStorage = UnifiedJsonStore(applicationContext)
        i("Pubspot started")
    }
}