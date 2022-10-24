package org.wit.pubspot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PubspotModel(var id: Long = 0,
                        var name: String = "",
                        var description: String = "",
                        var rating: Int = 0) : Parcelable
