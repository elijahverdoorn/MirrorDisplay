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

        cleanButton.setOnClickListener {
            clearSettings(prefs)
            clearFields()
        }

        save.setOnClickListener {
            onBackPressed()
        }
    }

    private fun clearFields() {
        quoteURL.editText?.text?.clear()
        weatherApiKey.editText?.text?.clear()
        weatherLat.editText?.text?.clear()
        weatherLon.editText?.text?.clear()
    }

    private fun clearSettings(prefs: SharedPreferences) {
        prefs.edit().clear().commit()
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
        if (fieldsSet()) {
            saveSettings(prefs)
            startMainActivity()
        } else {
            // need all values to proceed, so don't advance
            Snackbar.make(save, R.string.settings_not_all_fields_set, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, FullscreenActivity::class.java))
    }

    private fun setFromPrefs(prefs: SharedPreferences) {
        setSwitchesFromPrefs(prefs)

        quoteURL.editText?.setText(prefs.getString(getString(R.string.SHARED_PREFS_QUOTE_URL), ""))
        weatherApiKey.editText?.setText(prefs.getString(getString(R.string.SHARED_PREFS_WEATHER_API_KEY), ""))
        if (prefs.contains(getString(R.string.SHARED_PREFS_WEATHER_LAT))) {
            weatherLat.editText?.setText(prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LAT), 0f).toString())
        }
        if (prefs.contains(getString(R.string.SHARED_PREFS_WEATHER_LON))) {
            weatherLon.editText?.setText(prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LON), 0f).toString())
        }

        if (prefs.contains(getString(R.string.SHARED_PREFS_BIBLE_DURATION))) {
            bibleDuration.editText?.setText(prefs.getLong(getString(R.string.SHARED_PREFS_BIBLE_DURATION), 0).toString())
        }
        if (prefs.contains(getString(R.string.SHARED_PREFS_QUOTE_DURATION))) {
            bibleDuration.editText?.setText(prefs.getLong(getString(R.string.SHARED_PREFS_QUOTE_DURATION), 0).toString())
        }
    }

    private fun setSwitchesFromPrefs(prefs: SharedPreferences) {
        quoteSwitch.isChecked = prefs.getBoolean(getString(R.string.SHARED_PREFS_QUOTE_ENABLED), false)
        bibleSwitch.isChecked = prefs.getBoolean(getString(R.string.SHARED_PREFS_BIBLE_ENABLED), false)
        timeSwitch.isChecked = prefs.getBoolean(getString(R.string.SHARED_PREFS_TIME_ENABLED), false)
        weatherSwitch.isChecked = prefs.getBoolean(getString(R.string.SHARED_PREFS_WEATHER_ENABLED), false)
    }

    private fun fieldsSet() = quoteSet() && weatherSet()

    private fun quoteSet() = if (quoteSwitch.isChecked) {
            quoteURL.editText?.text.isNullOrBlank().not()
        } else true

    private fun weatherSet() = if (weatherSwitch.isChecked) {
            (weatherApiKey.editText?.text.isNullOrBlank().not()
                    && weatherLat.editText?.text.isNullOrBlank().not()
                    && weatherLon.editText?.text.isNullOrBlank().not())
        } else true

    private fun saveWeather(editor: SharedPreferences.Editor) {
        val lat = weatherLat.editText?.text.toString().toFloat()
        val lon = weatherLon.editText?.text.toString().toFloat()
        mapOf(
            getString(R.string.SHARED_PREFS_WEATHER_LAT) to lat,
            getString(R.string.SHARED_PREFS_WEATHER_LON) to lon
        ).forEach { key, value ->
            with(editor) {
                putFloat(key, value)
            }
        }
    }

    private fun saveQuote(editor: SharedPreferences.Editor) {
        val quote = quoteURL.editText?.text.toString()
        val apiKey = weatherApiKey.editText?.text.toString()
        val duration = quoteDuration.editText?.text.toString()
        mapOf(
            quote to getString(R.string.SHARED_PREFS_QUOTE_URL),
            apiKey to getString(R.string.SHARED_PREFS_WEATHER_API_KEY)
        ).forEach { value, key ->
            with(editor) {
                putString(key, value)
            }
        }
        with(editor) {
            putLong(getString(R.string.SHARED_PREFS_QUOTE_DURATION), duration.toLong())
        }
    }

    private fun saveBible(editor: SharedPreferences.Editor) {
        val duration = bibleDuration.editText?.text.toString()
        with(editor) {
            putLong(getString(R.string.SHARED_PREFS_BIBLE_DURATION), duration.toLong())
        }
    }

    private fun saveEnabled(editor: SharedPreferences.Editor) {
        val quoteEnabled = quoteSwitch.isChecked
        val bibleEnabled = bibleSwitch.isChecked
        val weatherEnabled = weatherSwitch.isChecked
        val timeEnabled = timeSwitch.isChecked
        mapOf(
            getString(R.string.SHARED_PREFS_QUOTE_ENABLED) to quoteEnabled,
            getString(R.string.SHARED_PREFS_BIBLE_ENABLED) to bibleEnabled,
            getString(R.string.SHARED_PREFS_WEATHER_ENABLED) to weatherEnabled,
            getString(R.string.SHARED_PREFS_TIME_ENABLED) to timeEnabled
        ).forEach { key, value ->
            with(editor) {
                putBoolean(key, value)
            }
        }
    }

    private fun saveSettings(prefs: SharedPreferences) {
        val editor = prefs.edit()

        saveEnabled(editor)
        if (quoteSwitch.isChecked) {
            saveQuote(editor)
        }
        if (weatherSwitch.isChecked) {
            saveWeather(editor)
        }
        saveBible(editor)

        editor.commit()
    }
}
