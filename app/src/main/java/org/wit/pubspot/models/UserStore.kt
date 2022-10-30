package org.wit.pubspot.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun findOne(user: UserModel): UserModel?
    fun findByEmail(email: String): UserModel?
    fun deleteAll()
}