package com.elijahverdoorn.mirrordisplay.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.component.BibleComponent
import com.elijahverdoorn.mirrordisplay.component.QuoteComponent
import com.elijahverdoorn.mirrordisplay.component.TimeComponent
import com.elijahverdoorn.mirrordisplay.component.WeatherComponent
import com.elijahverdoorn.mirrordisplay.data.manager.BibleManager
import com.elijahverdoorn.mirrordisplay.data.manager.QuoteManager
import com.elijahverdoorn.mirrordisplay.data.manager.WeatherManager
import kotlinx.android.synthetic.main.activity_fullscreen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    private lateinit var prefs: SharedPreferences

    @kotlin.time.ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }

        prefs = applicationContext.getSharedPreferences(getString(R.string.SHARED_PREFS_FILE_KEY), Context.MODE_PRIVATE)

        if (sharedPrefsSet(prefs)) {
            setupUI(prefs)
        } else {
            // Need settings
            launchSettings()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.settings -> {
                launchSettings()
                true
            }
            R.id.about -> {
                launchAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sharedPrefsSet(sharedPreferences: SharedPreferences): Boolean {
        return weatherPrefsSet(sharedPreferences) && quotePrefsSet(prefs)
    }

    private fun weatherPrefsSet(prefs: SharedPreferences): Boolean {
        return componentPrefsSet(prefs, getString(R.string.SHARED_PREFS_WEATHER_ENABLED), listOf(
            getString(R.string.SHARED_PREFS_WEATHER_LON),
            getString(R.string.SHARED_PREFS_WEATHER_LAT),
            getString(R.string.SHARED_PREFS_WEATHER_API_KEY)
        ))
    }

    private fun quotePrefsSet(prefs: SharedPreferences): Boolean {
        return componentPrefsSet(prefs, getString(R.string.SHARED_PREFS_QUOTE_ENABLED), listOf(
            getString(R.string.SHARED_PREFS_QUOTE_URL)
        ))
    }

    private fun componentPrefsSet(prefs: SharedPreferences, enabledKey: String, requiredVals: List<String>): Boolean {
        if (prefs.getBoolean(enabledKey, false)) {
            requiredVals.forEach {
                if (!prefs.contains(it)) {
                    return false
                }
            }
        }
        return true
    }

    @kotlin.time.ExperimentalTime
    private fun setupUI(prefs: SharedPreferences) {
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_WEATHER_ENABLED), false)) {
            makeWeatherComponent(
                prefs.getString(getString(R.string.SHARED_PREFS_WEATHER_API_KEY), "")!!,
                prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LAT), 0f),
                prefs.getFloat(getString(R.string.SHARED_PREFS_WEATHER_LON), 0f)
            )
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_QUOTE_ENABLED), false)) {
            makeQuoteComponent(
                prefs.getString(getString(R.string.SHARED_PREFS_QUOTE_URL), "")!!,
                getDuration(getString(R.string.SHARED_PREFS_QUOTE_DURATION))
            )
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_TIME_ENABLED), false)) {
            makeTimeComponent()
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_BIBLE_ENABLED), false)) {
            makeBibleComponent(getDuration(getString(R.string.SHARED_PREFS_BIBLE_DURATION)))
        }
    }

    @kotlin.time.ExperimentalTime
    private fun getDuration(key: String): Duration {
        val l = prefs.getLong(key, TEN_SECONDS.toLong())
        return l.toDuration(DurationUnit.MILLISECONDS)
    }

    private fun launchSettings() {
        cancel()
        finish()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun launchAbout() {
        cancel()
        finish()
        startActivity(Intent(this, AboutActivity::class.java))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    @kotlin.time.ExperimentalTime
    private fun makeBibleComponent(duration: Duration) {
        val bibleComponent = BibleComponent(this)
        val bibleManager = BibleManager()
        launch {
            bibleComponent.update(bibleManager, duration)
        }
        bibleFrame.addView(bibleComponent)
    }

    private fun makeWeatherComponent(
        apiKey: String,
        lat: Float,
        lon: Float
    ) {
        val weatherComponent = WeatherComponent(this)
        val weatherManager = WeatherManager(coroutineContext, apiKey, lat, lon)
        launch {
            weatherComponent.update(weatherManager)
        }
        weatherFrame.addView(weatherComponent)
    }

    private fun makeTimeComponent() {
        val timeComponent = TimeComponent(this)
        launch {
            timeComponent.update()
        }
        timeFrame.addView(timeComponent)
    }

    @kotlin.time.ExperimentalTime
    private fun makeQuoteComponent(quoteUrl: String, duration: Duration) {
        val quoteComponent = QuoteComponent(this)
        val quoteManager =
            QuoteManager(
                quoteUrl
            )
        launch {
            quoteComponent.update(quoteManager, duration)
        }
        quoteFrame.addView(quoteComponent)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300

        @kotlin.time.ExperimentalTime
        private const val TEN_SECONDS = 10_000
    }
}
