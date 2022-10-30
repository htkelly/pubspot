package org.wit.pubspot.models

import timber.log.Timber.i

var lastUserId = 0L

internal fun getUserId(): Long {
    return lastUserId++
}

class UserMemStore : UserStore {

    val users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        user.id = getUserId()
        users.add(user)
        logAll()
    }

    override fun findOne(user: UserModel) : UserModel? {
        return users.find {u -> u.id == user.id }
    }

    override fun findByEmail(email: String) : UserModel? {
        return users.find {u -> u.email == email}
    }

    override fun deleteAll() {
        users.clear()
        lastUserId = 0L
        logAll()
    }

    fun logAll() {
        users.forEach{ i("${it}") }
    }
}