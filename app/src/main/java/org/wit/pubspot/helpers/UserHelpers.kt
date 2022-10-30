package org.wit.pubspot.helpers

import org.wit.pubspot.models.UnifiedStore
import org.wit.pubspot.models.UserModel
import org.wit.pubspot.models.UserStore

fun authenticate(email: String, password: String, storage: UnifiedStore) : UserModel? {
    val user = storage.findUserByEmail(email)
    if (user != null) {
        if(user.password == password) return user
    }
    return null
}