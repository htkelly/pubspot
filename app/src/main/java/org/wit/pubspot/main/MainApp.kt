package org.wit.pubspot.main

import android.app.Application
import org.wit.pubspot.models.PubspotJsonStore
import org.wit.pubspot.models.PubspotMemStore
import org.wit.pubspot.models.PubspotStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var pubs: PubspotStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        pubs = PubspotJsonStore(applicationContext)
        i("Pubspot started")
    }
}