package com.elijahverdoorn.mirrordisplay.manager

import android.content.Context
import android.content.SharedPreferences
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.activity.FullscreenActivity
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

class PrefsManager(
    val appContext: Context
) {
    val prefs: SharedPreferences

    init {
        prefs = appContext.getSharedPreferences(appContext.getString(R.string.SHARED_PREFS_FILE_KEY), Context.MODE_PRIVATE)
    }

    fun sharedPrefsSet(): Boolean {
        return weatherPrefsSet() && quotePrefsSet()
    }

    @ExperimentalTime
    fun getDuration(key: String): Duration {
        val l = prefs.getLong(key, FullscreenActivity.TEN_SECONDS.toLong())
        return l.toDuration(DurationUnit.MINUTES)
    }

    private fun weatherPrefsSet(): Boolean {
        return componentPrefsSet(appContext.getString(R.string.SHARED_PREFS_WEATHER_ENABLED), listOf(
            appContext.getString(R.string.SHARED_PREFS_WEATHER_LON),
            appContext.getString(R.string.SHARED_PREFS_WEATHER_LAT),
            appContext.getString(R.string.SHARED_PREFS_WEATHER_API_KEY)
        ))
    }

    private fun quotePrefsSet(): Boolean {
        return componentPrefsSet(appContext.getString(R.string.SHARED_PREFS_QUOTE_ENABLED), listOf(
            appContext.getString(R.string.SHARED_PREFS_QUOTE_URL)
        ))
    }

    private fun componentPrefsSet(enabledKey: String, requiredVals: List<String>): Boolean {
        if (prefs.getBoolean(enabledKey, false)) {
            requiredVals.forEach {
                if (!prefs.contains(it)) {
                    return false
                }
            }
        }
        return true
    }
}