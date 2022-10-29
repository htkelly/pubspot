package org.wit.pubspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityTagBinding
import org.wit.pubspot.models.PubspotModel

class TagActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarTag)

        if (intent.hasExtra("pubspot_edit")) {
            val pub = intent.extras?.getParcelable<PubspotModel>("pubspot_edit")!!
            if (pub.tags.contains("Wi-Fi")) binding.checkboxTagWifi.isChecked = true
            if (pub.tags.contains("Wheelchair Accessible")) binding.checkboxTagWheelchairAccessible.isChecked = true
            if (pub.tags.contains("Dog Friendly")) binding.checkboxTagDogFriendly.isChecked = true
            if (pub.tags.contains("Food Served")) binding.checkboxTagFoodServed.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tag, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tag_cancel -> {
                finish()
            }
            R.id.tag_done -> {
                val resultIntent = Intent()
                val tags = ArrayList<String>()
                if (binding.checkboxTagWifi.isChecked) tags.add("Wi-Fi")
                if (binding.checkboxTagWheelchairAccessible.isChecked) tags.add("Wheelchair Accessible")
                if (binding.checkboxTagDogFriendly.isChecked) tags.add("Dog Friendly")
                if (binding.checkboxTagFoodServed.isChecked) tags.add("Food Served")
                resultIntent.putExtra("tags", tags)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}