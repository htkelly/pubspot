package org.wit.pubspot.models

interface UnifiedStore {
    fun findAllUsers(): List<UserModel>
    fun createUser(user: UserModel)
    fun findOneUser(user: UserModel): UserModel?
    fun findUserByEmail(email: String): UserModel?
    fun deleteAllUsers()
    fun findAllUserPubs(user: UserModel): List<PubspotModel>
    fun createUserPub(user: UserModel, pub: PubspotModel)
    fun updateUserPub(user: UserModel, pub: PubspotModel)
    fun deleteUserPub(user: UserModel, pub: PubspotModel)
    fun deleteAllUserPubs(user: UserModel)
}