package com.elijahverdoorn.mirrordisplay.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
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
                startActivity(getBrowserIntent(getString(R.string.privacy_policy_url)))
            }
        }

        termsOfService.apply {
            setOnClickListener {
                startActivity(getBrowserIntent(getString(R.string.terms_of_service_url)))
            }
        }

        contactDeveloper.setOnClickListener {
            startActivity(getEmailDeveloperIntent())
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

    private fun getEmailDeveloperIntent() = Intent(Intent.ACTION_SEND).apply {
        type = "*/*"
        putExtra(Intent.EXTRA_EMAIL, getString(R.string.developer_email))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.email_footer_text, BuildConfig.VERSION_NAME, getDeviceInfo(), getAndroidVersion()) )
    }

    private fun getDeviceInfo(): String {
        val manu = Build.MANUFACTURER
        val model = Build.MODEL
        return "Manufacturer: $manu, Model: $model"
    }

    private fun getAndroidVersion(): String {
        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        return "Android SDK: $sdkVersion ($release)"
    }

    private fun getBrowserIntent(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))
}
