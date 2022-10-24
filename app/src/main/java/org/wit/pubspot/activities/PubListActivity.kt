package org.wit.pubspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityPubListBinding
import org.wit.pubspot.databinding.CardPubBinding
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.PubspotModel

class PubListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPubListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPubListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PubspotAdapter(app.pubs)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PubspotActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

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