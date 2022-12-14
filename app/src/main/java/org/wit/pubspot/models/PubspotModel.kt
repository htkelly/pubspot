package org.wit.pubspot.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PubspotModel(var id: Long = 0,
                        var name: String = "",
                        var description: String = "",
                        var rating: Int = 0,
                        var image: Uri = Uri.EMPTY,
                        var tags: ArrayList<String> = ArrayList(),
                        var lat: Double = 0.0,
                        var lng: Double = 0.0,
                        var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
