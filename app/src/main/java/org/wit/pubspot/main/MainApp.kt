package org.wit.pubspot.main

import android.app.Application
import org.wit.pubspot.models.PubspotJsonStore
import org.wit.pubspot.models.PubspotMemStore
import org.wit.pubspot.models.PubspotStore
import org.wit.pubspot.models.UserMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var pubs: PubspotStore
    val users = UserMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        pubs = PubspotJsonStore(applicationContext)
        i("Pubspot started")
    }
}