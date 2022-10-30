package org.wit.pubspot.helpers

import org.wit.pubspot.models.UserModel
import org.wit.pubspot.models.UserStore

fun authenticate(email: String, password: String, users: UserStore) : UserModel? {
    val user = users.findByEmail(email)
    if (user != null) {
        if(user.password == password) return user
    }
    return null
}