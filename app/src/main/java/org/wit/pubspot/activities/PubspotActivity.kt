package org.wit.pubspot.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityPubspotBinding
import org.wit.pubspot.helpers.showImagePicker
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.Location
import org.wit.pubspot.models.PubspotModel
import timber.log.Timber.i

class PubspotActivity : AppCompatActivity() {

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
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

        registerImagePickerCallback()
        registerMapCallback()

        app = application as MainApp

        i("Pubspot Activity started...")

        if (intent.hasExtra("pubspot_edit")) {
            edit = true
            pub = intent.extras?.getParcelable("pubspot_edit")!!
            binding.pubName.setText(pub.name)
            binding.description.setText(pub.description)
            binding.rating.rating = pub.rating.toFloat()
            binding.btnAdd.setText(R.string.save_pub)
            Picasso.get()
                .load(pub.image)
                .into(binding.pubImage)
            if (pub.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_pub_image)
            }
        }

        binding.btnAdd.setOnClickListener {
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
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.chooseImage.setOnClickListener {
            i("Launching image picker")
            showImagePicker(imageIntentLauncher)
        }

        binding.pubLocation.setOnClickListener {
            val location = Location(52.65049980615224, -7.249279125737909, 15f)
            if (pub.zoom != 0f) {
                location.lat = pub.lat
                location.lng = pub.lng
                location.zoom = pub.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pub, menu)
        if (!intent.hasExtra("pubspot_edit")) {
            menu.findItem(R.id.item_delete).isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                pub = intent.extras?.getParcelable("pubspot_edit")!!
                app.pubs.delete(pub)
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            pub.image = result.data!!.data!!
                            Picasso.get()
                                .load(pub.image)
                                .into(binding.pubImage)
                            binding.chooseImage.setText(R.string.change_pub_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            pub.lat = location.lat
                            pub.lng = location.lng
                            pub.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}