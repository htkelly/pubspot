package org.wit.pubspot.main

import android.app.Application
import org.wit.pubspot.models.PubspotModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    val pubs = ArrayList<PubspotModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Pubspot started")
    }
}