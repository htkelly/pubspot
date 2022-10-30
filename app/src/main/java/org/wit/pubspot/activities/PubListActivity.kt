package org.wit.pubspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.pubspot.R
import org.wit.pubspot.adapters.PubspotAdapter
import org.wit.pubspot.adapters.PubspotListener
import org.wit.pubspot.databinding.ActivityPubListBinding
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.PubspotModel

class PubListActivity : AppCompatActivity(), PubspotListener {

    lateinit var app: MainApp
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityPubListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadPubs() }
    }

    private fun loadPubs() {
        if (app.loggedInUser != null) {
            showPubs(app.unifiedStorage.findAllUserPubs(app.loggedInUser!!))
        }
        else finish()
    }

    private fun showPubs(pubs: List<PubspotModel>) {
        binding.pubListContent.recyclerView.adapter = PubspotAdapter(pubs, this)
        binding.pubListContent.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPubListBinding.inflate(layoutInflater)
        binding.pubListContent.toolbar.title = title
        setSupportActionBar(binding.pubListContent.toolbar)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.pubListContent.recyclerView.layoutManager = layoutManager
        loadPubs()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PubspotActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_account -> {
                //TODO: open nav drawer here
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPubspotClick(pub: PubspotModel) {
        val launcherIntent = Intent(this, PubspotActivity::class.java)
        launcherIntent.putExtra("pubspot_edit", pub)
        refreshIntentLauncher.launch(launcherIntent)
    }
}