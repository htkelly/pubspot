package org.wit.pubspot.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PubspotMemStore : PubspotStore {

    val pubs = ArrayList<PubspotModel>()

    override fun findAll(): List<PubspotModel> {
        return pubs
    }

    override fun create(pub: PubspotModel) {
        pub.id = getId()
        pubs.add(pub)
        logAll()
    }

    override fun update(pub: PubspotModel) {
        var foundPub: PubspotModel? = pubs.find { p -> p.id == pub.id }
        if (foundPub != null) {
            foundPub.name = pub.name
            foundPub.description = pub.description
            foundPub.rating = pub.rating
            foundPub.image = pub.image
            foundPub.lat = pub.lat
            foundPub.lng = pub.lng
            foundPub.zoom = pub.zoom
            logAll()
        }
    }

    override fun delete(pub: PubspotModel) {
        pubs.remove(pub)
    }

    override fun deleteAll() {
        pubs.clear()
        logAll()
    }

    fun logAll() {
        pubs.forEach{ i("${it}") }
    }

}