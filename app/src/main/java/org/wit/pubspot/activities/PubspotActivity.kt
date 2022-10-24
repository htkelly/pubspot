package org.wit.pubspot.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityPubspotBinding
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.PubspotModel
import timber.log.Timber
import timber.log.Timber.i

class PubspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPubspotBinding
    var pub = PubspotModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityPubspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Pubspot Activity started...")

        if (intent.hasExtra("pubspot_edit")) {
            edit = true
            pub = intent.extras?.getParcelable("pubspot_edit")!!
            binding.pubName.setText(pub.name)
            binding.description.setText(pub.description)
            binding.rating.rating = pub.rating.toFloat()
            binding.btnAdd.setText(R.string.save_pub)
        }

        binding.btnAdd.setOnClickListener() {
            pub.name = binding.pubName.text.toString()
            pub.description = binding.description.text.toString()
            pub.rating = binding.rating.rating.toInt()
            if (pub.name.isEmpty()) {
                Snackbar.make(it, R.string.enter_pub_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.pubs.update(pub.copy())
                } else {
                    app.pubs.create(pub.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pub, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}