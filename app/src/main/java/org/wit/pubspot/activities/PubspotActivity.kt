package org.wit.pubspot.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityPubspotBinding
import org.wit.pubspot.dialogs.SetImageDialog
import org.wit.pubspot.helpers.createNewImageFile
import org.wit.pubspot.helpers.showImagePicker
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.Location
import org.wit.pubspot.models.PubspotModel
import timber.log.Timber.i
import java.io.File

class PubspotActivity : AppCompatActivity(), SetImageDialog.SetImageDialogListener {

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var tagIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityPubspotBinding
    var cameraPhotoFilePath: Uri? = null
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
        registerCameraCallback()
        registerMapCallback()
        registerTagCallback()

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
                    app.unifiedStorage.updateUserPub(app.loggedInUser!!, pub.copy())
                } else {
                    app.unifiedStorage.createUserPub(app.loggedInUser!!, pub.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.chooseImage.setOnClickListener {
            showSetImageDialog()
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

        binding.setTags.setOnClickListener {
            i("Launching tag selector activity")
            val launcherIntent = Intent(this, TagActivity::class.java)
            if (edit) {
                launcherIntent.putExtra("pubspot_edit", pub)
            }
            tagIntentLauncher.launch(launcherIntent)
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
                app.unifiedStorage.deleteUserPub(app.loggedInUser!!, pub)
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

    private fun registerCameraCallback() {
        cameraIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (cameraPhotoFilePath != null) {
                            pub.image = cameraPhotoFilePath as Uri
                            Picasso.get()
                                .load(pub.image)
                                .into(binding.pubImage)
                            binding.chooseImage.setText(R.string.change_pub_image)
                            cameraPhotoFilePath = null
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

    private fun registerTagCallback() {
        tagIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if(result.data != null){
                            val tags = result.data!!.extras?.get("tags")
                            i("Tags: ${tags.toString()}")
                            pub.tags = tags as ArrayList<String>
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun showSetImageDialog() {
        val choiceDialog = SetImageDialog()
        choiceDialog.show(this.supportFragmentManager, "SetImageDialogFragment")
    }

    override fun onSelectChooseImage(dialog: DialogFragment) {
        i("Launching image picker")
        showImagePicker(imageIntentLauncher)
    }

    override fun onSelectTakePhoto(dialog: DialogFragment) {
        i("Launching camera activity")
        showCamera(cameraIntentLauncher)
    }

//ideally this should be in ImageHelpers.kt -- need to figure out how to recover the Uri when calling this function from outside this class
    private fun showCamera (intentLauncher : ActivityResultLauncher<Intent>) {
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createNewImageFile(this)
        val photoURI: Uri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
        cameraPhotoFilePath = photoURI
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        intentLauncher.launch(takePhoto)
    }
}