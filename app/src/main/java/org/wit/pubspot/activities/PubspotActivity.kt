package org.wit.pubspot.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.pubspot.databinding.ActivityPubspotBinding
import org.wit.pubspot.models.PubspotModel
import timber.log.Timber
import timber.log.Timber.i

class PubspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPubspotBinding
    var pub = PubspotModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPubspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("Pubspot Activity started...")

        binding.btnAdd.setOnClickListener() {
            pub.name = binding.pubName.text.toString()
            if (pub.name.isNotEmpty()) {
                i("Added pub: ${pub.name}")
            }
            else {
                Snackbar
                    .make(it,"Please enter a pub name", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}