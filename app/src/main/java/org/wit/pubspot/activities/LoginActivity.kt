package org.wit.pubspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.pubspot.R
import org.wit.pubspot.databinding.ActivityLoginBinding
import org.wit.pubspot.helpers.authenticate
import org.wit.pubspot.main.MainApp
import org.wit.pubspot.models.UserModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var pubListIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loginToolbar.title = title
        setContentView(binding.root)

        val app = application as MainApp

        registerPubListIntentLauncher()

        binding.signupBtn.setOnClickListener(){
            val signupUser = UserModel(email = binding.loginEmail.text.toString(), password = binding.loginPassword.text.toString())
            val launcherIntent = Intent(this, PubListActivity::class.java)
            app.unifiedStorage.createUser(signupUser)
            app.loggedInUser = signupUser
            pubListIntentLauncher.launch(launcherIntent)
        }

        binding.loginBtn.setOnClickListener(){
            val loginUser = authenticate(binding.loginEmail.text.toString(), binding.loginPassword.text.toString(), app.unifiedStorage)
            val launcherIntent = Intent(this, PubListActivity::class.java)
            if (loginUser == null) {
                Snackbar.make(it, R.string.login_error, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                app.loggedInUser = loginUser
                pubListIntentLauncher.launch(launcherIntent)
            }
        }
    }

    private fun registerPubListIntentLauncher() {
        pubListIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        //This doesn't do anything right now, but might be useful later
        { result ->
            when(result.resultCode){
                RESULT_OK -> {}
                RESULT_CANCELED -> { }
                else -> { }
            }
        }
    }

}