package com.elijahverdoorn.mirrordisplay.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.BuildConfig
import com.elijahverdoorn.mirrordisplay.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = resources.getString(R.string.title_activity_about, getString(R.string.app_name))
        appVersion.text = resources.getString(R.string.version_template, BuildConfig.VERSION_NAME)

        privacyPolicy.apply {
            setOnClickListener {
                getBrowserIntent(getString(R.string.privacy_policy_url))
            }
        }

        termsOfService.apply {
            setOnClickListener {
                getBrowserIntent(getString(R.string.terms_of_service_url))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getBrowserIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)

    }
}
