package org.wit.pubspot.models

interface PubspotStore {
    fun findAll(): List<PubspotModel>
    fun create(pub: PubspotModel)
}