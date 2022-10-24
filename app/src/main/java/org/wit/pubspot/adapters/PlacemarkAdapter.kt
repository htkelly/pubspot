package org.wit.pubspot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.pubspot.databinding.CardPubBinding
import org.wit.pubspot.models.PubspotModel

class PubspotAdapter constructor(private var pubs: List<PubspotModel>) : RecyclerView.Adapter<PubspotAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPubBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val pub = pubs[holder.adapterPosition]
        holder.bind(pub)
    }

    override fun getItemCount(): Int = pubs.size

    class MainHolder(private val binding : CardPubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pub: PubspotModel) {
            binding.pubName.text = pub.name
            binding.description.text = pub.description
            binding.rating.rating = pub.rating.toFloat()
        }
    }

}