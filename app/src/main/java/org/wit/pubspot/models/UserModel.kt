package org.wit.pubspot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                        var email: String = "",
                        var password: String = "",
                        var pubs : ArrayList<PubspotModel> = ArrayList()) : Parcelable

