package com.elijahverdoorn.mirrordisplay.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.BuildConfig
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.util.IntentUtil
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
                startActivity(IntentUtil.getBrowserIntent(getString(R.string.privacy_policy_url)))
            }
        }

        termsOfService.apply {
            setOnClickListener {
                startActivity(IntentUtil.getBrowserIntent(getString(R.string.terms_of_service_url)))
            }
        }

        contactDeveloper.setOnClickListener {
            startActivity(IntentUtil.getEmailDeveloperIntent(this))
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

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this, FullscreenActivity::class.java))
    }
}
