package com.elijahverdoorn.mirrordisplay.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = applicationContext.getSharedPreferences(getString(R.string.SHARED_PREFS_FILE_KEY), Context.MODE_PRIVATE)

        setFromPrefs(prefs)

        save.setOnClickListener {
            if (fieldsSet()) {
                saveSettings(prefs)
                startActivity(Intent(this, FullscreenActivity::class.java))
            } else {
                // need all values to proceed, so don't advance
                Snackbar.make(save, R.string.settings_not_all_fields_set, Snackbar.LENGTH_SHORT).show();
            }
        }
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
        mapOf(
            quoteURL.editText?.text.toString() to getString(R.string.SHARED_PREFS_QUOTE_URL),
            weatherApiKey.editText?.text.toString() to getString(R.string.SHARED_PREFS_WEATHER_API_KEY)
        ).forEach { value, key ->
            with(editor) {
                putString(key, value)
            }
        }

        mapOf(
            weatherLat.editText?.text.toString().toFloat() to getString(R.string.SHARED_PREFS_WEATHER_LAT),
            weatherLon.editText?.text.toString().toFloat() to getString(R.string.SHARED_PREFS_WEATHER_LON)
        ).forEach { value, key ->
            with(editor) {
                putFloat(key, value)
            }
        }
        editor.commit()
    }
}
