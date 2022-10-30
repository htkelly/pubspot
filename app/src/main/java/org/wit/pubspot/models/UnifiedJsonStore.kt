package org.wit.pubspot.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.pubspot.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE_ = "unifiedUserPubspot.json"

val gsonBuilder_: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser_())
    .create()
val listType_: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomId_(): Long {
    return Random().nextLong()
}

class UnifiedJsonStore(private val context: Context) : UnifiedStore {
    var users = mutableListOf<UserModel>()

    init {
        if (exists(context, JSON_FILE_)) {
            deserialize()
        }
    }

    override fun findAllUsers(): List<UserModel> {
        logAll()
        return users
    }

    override fun createUser(user: UserModel) {
        user.id = generateRandomId_()
        users.add(user)
        serialize()
    }

    override fun findOneUser(user: UserModel): UserModel? {
        return users.find {u -> u.id == user.id }
    }

    override fun findUserByEmail(email: String): UserModel? {
        return users.find {u -> u.email == email}
    }

    override fun deleteAllUsers() {
        users.clear()
        serialize()
    }

    override fun findAllUserPubs(user: UserModel): MutableList<PubspotModel> {
        logAll()
        return user.pubs
    }

    override fun createUserPub(user: UserModel, pub: PubspotModel) {
        pub.id = generateRandomId_()
        user.pubs.add(pub)
        serialize()
    }

    override fun updateUserPub(user: UserModel, pub: PubspotModel) {
        val foundPub: PubspotModel? = user.pubs.find { p -> p.id == pub.id }
        if (foundPub != null) {
            foundPub.name = pub.name
            foundPub.description = pub.description
            foundPub.rating = pub.rating
            foundPub.image = pub.image
            foundPub.tags = pub.tags
            foundPub.lat = pub.lat
            foundPub.lng = pub.lng
            foundPub.zoom = pub.zoom
            serialize()
        }
    }

    override fun deleteUserPub(user: UserModel, pub: PubspotModel) {
        user.pubs.remove(pub)
        serialize()
    }

    override fun deleteAllUserPubs(user: UserModel) {
        user.pubs.clear()
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder_.toJson(users, listType_)
        write(context, JSON_FILE_, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE_)
        users = gsonBuilder_.fromJson(jsonString, listType_)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}

class UriParser_ : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}