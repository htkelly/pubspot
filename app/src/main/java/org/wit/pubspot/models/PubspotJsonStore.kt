package org.wit.pubspot.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.pubspot.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "pubspot.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<PubspotModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PubspotJsonStore(private val context: Context) : PubspotStore {
    var pubs = mutableListOf<PubspotModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PubspotModel> {
        logAll()
        return pubs
    }

    override fun create(pub: PubspotModel) {
        pub.id = generateRandomId()
        pubs.add(pub)
        serialize()
    }

    override fun update(pub: PubspotModel) {
        val foundPub: PubspotModel? = pubs.find { p -> p.id == pub.id }
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

    override fun delete(pub: PubspotModel) {
        pubs.remove(pub)
        serialize()
    }

    override fun deleteAll() {
        pubs.clear()
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(pubs, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        pubs = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        pubs.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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