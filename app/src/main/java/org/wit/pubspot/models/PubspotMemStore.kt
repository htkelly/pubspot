package org.wit.pubspot.models

import timber.log.Timber.i

class PubspotMemStore : PubspotStore {

    val pubs = ArrayList<PubspotModel>()

    override fun findAll(): List<PubspotModel> {
        return pubs
    }

    override fun create(pub: PubspotModel) {
        pubs.add(pub)
    }

    fun logAll() {
        pubs.forEach{ i("${it}") }
    }
}