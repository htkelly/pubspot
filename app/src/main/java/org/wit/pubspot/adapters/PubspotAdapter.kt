package org.wit.pubspot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.pubspot.databinding.CardPubBinding
import org.wit.pubspot.models.PubspotModel

interface PubspotListener {
    fun onPubspotClick(pub: PubspotModel)
}

class PubspotAdapter constructor(private var pubs: List<PubspotModel>, private val listener: PubspotListener) : RecyclerView.Adapter<PubspotAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPubBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val pub = pubs[holder.adapterPosition]
        holder.bind(pub, listener)
    }

    override fun getItemCount(): Int = pubs.size

    class MainHolder(private val binding : CardPubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pub: PubspotModel, listener: PubspotListener) {
            binding.pubName.text = pub.name
            binding.description.text = pub.description
            binding.rating.rating = pub.rating.toFloat()
            Picasso.get().load(pub.image).resize(200,200).into(binding.imageIcon)
            if (!pub.tags.contains("Wi-Fi")) binding.tagWifi.isVisible = false
            if (!pub.tags.contains("Wheelchair Accessible")) binding.tagWheelchairAccessible.isVisible = false
            if (!pub.tags.contains("Dog Friendly")) binding.tagDogFriendly.isVisible = false
            if (!pub.tags.contains("Food Served")) binding.tagFoodServed.isVisible = false
            binding.root.setOnClickListener { listener.onPubspotClick(pub) }
        }
    }

}