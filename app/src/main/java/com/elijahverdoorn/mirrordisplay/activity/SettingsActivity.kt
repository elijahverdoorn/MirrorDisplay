package com.elijahverdoorn.mirrordisplay.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = applicationContext.getSharedPreferences(getString(R.string.SHARED_PREFS_FILE_KEY), Context.MODE_PRIVATE)
        setFromPrefs(prefs)

        save.setOnClickListener {
            if (fieldsSet()) {
                saveSettings(prefs)
                startMainActivity()
            } else {
                // need all values to proceed, so don't advance
                Snackbar.make(save, R.string.settings_not_all_fields_set, Snackbar.LENGTH_SHORT).show();
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

    override fun onBackPressed() {
        saveSettings(prefs)
        startMainActivity()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, FullscreenActivity::class.java))
    }

    private fun setFromPrefs(prefs: SharedPreferences) {
        quoteURL.editText?.setText(prefs.getString(getString(R.string.SHARED_PREFS_QUOTE_URL), ""))
        weatherApiKey.editText?.setText(prefs.getString(getString(R.string.SHARED_PREFS_WEATHER_API_KEY), ""))
        weatherLat.editText?.setText(prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LAT), 0f).toString())
        weatherLon.editText?.setText(prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LON),  0f).toString())
    }

    private fun fieldsSet(): Boolean {
        if (
            quoteURL.editText?.text.isNullOrBlank() ||
            weatherApiKey.editText?.text.isNullOrBlank() ||
            weatherLat.editText?.text.isNullOrBlank() ||
            weatherLon.editText?.text.isNullOrBlank()
        ) {
            return false
        } else {
            return true
        }
    }

    private fun saveSettings(prefs: SharedPreferences) {
        val editor = prefs.edit()

        val quote = quoteURL.editText?.text.toString()
        val apiKey = weatherApiKey.editText?.text.toString()
        mapOf(
            quote to getString(R.string.SHARED_PREFS_QUOTE_URL),
            apiKey to getString(R.string.SHARED_PREFS_WEATHER_API_KEY)
        ).forEach { value, key ->
            with(editor) {
                putString(key, value)
            }
        }

        val lat = weatherLat.editText?.text.toString().toFloat()
        val lon = weatherLon.editText?.text.toString().toFloat()
        mapOf(
             lat to getString(R.string.SHARED_PREFS_WEATHER_LAT),
             lon to getString(R.string.SHARED_PREFS_WEATHER_LON)
        ).forEach { value, key ->
            with(editor) {
                putFloat(key, value)
            }
        }
        editor.commit()
    }
}
