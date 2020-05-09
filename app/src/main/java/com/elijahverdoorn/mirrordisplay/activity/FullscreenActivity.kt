package com.elijahverdoorn.mirrordisplay.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.component.Component
import com.elijahverdoorn.mirrordisplay.manager.ComponentManager
import com.elijahverdoorn.mirrordisplay.manager.PrefsManager
import kotlinx.android.synthetic.main.activity_fullscreen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.hours

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
    private lateinit var prefsManager: PrefsManager
    private lateinit var componentManager: ComponentManager

    @kotlin.time.ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }

        prefsManager = PrefsManager(applicationContext)
        componentManager = ComponentManager(prefsManager, this, coroutineContext)

        if (prefsManager.sharedPrefsSet()) {
            setupUI(prefsManager.prefs)
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

    @kotlin.time.ExperimentalTime
    private fun setupUI(prefs: SharedPreferences) {
        val components = mutableListOf<Component>()
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_WEATHER_ENABLED), false)) {
            componentManager.getWeatherComponent().let {
                weatherFrame.addView(it)
                components.add(it)
            }
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_QUOTE_ENABLED), false)) {
            componentManager.getQuoteComponent().let {
                quoteFrame.addView(it)
                components.add(it)
            }
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_TIME_ENABLED), false)) {
            componentManager.getTimeComponent().let {
                timeFrame.addView(it)
                components.add(it)
            }
        }
        if (prefs.getBoolean(getString(R.string.SHARED_PREFS_BIBLE_ENABLED), false)) {
            componentManager.getBibleComponent().let {
                bibleFrame.addView(it)
                components.add(it)
            }
        }

        components.forEach { launch { it.update() } }
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
        const val TEN_SECONDS = 10_000
        @ExperimentalTime
        val ONE_HOUR = 1.hours
    }
}
