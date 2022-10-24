package org.wit.pubspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.pubspot.R
import org.wit.pubspot.adapters.PubspotAdapter
import org.wit.pubspot.adapters.PubspotListener
import org.wit.pubspot.databinding.ActivityPubListBinding
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.PubspotModel

class PubListActivity : AppCompatActivity(), PubspotListener {

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
        binding.recyclerView.adapter = PubspotAdapter(app.pubs.findAll(), this)
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

    override fun onPubspotClick(pub: PubspotModel) {
        val launcherIntent = Intent(this, PubspotActivity::class.java)
        launcherIntent.putExtra("pubspot_edit", pub)
        startActivityForResult(launcherIntent, 0)
    }
}