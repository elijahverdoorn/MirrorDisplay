package com.elijahverdoorn.mirrordisplay.manager

import android.content.Context
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.activity.FullscreenActivity
import com.elijahverdoorn.mirrordisplay.component.BibleComponent
import com.elijahverdoorn.mirrordisplay.component.QuoteComponent
import com.elijahverdoorn.mirrordisplay.component.TimeComponent
import com.elijahverdoorn.mirrordisplay.component.WeatherComponent
import com.elijahverdoorn.mirrordisplay.data.manager.BibleManager
import com.elijahverdoorn.mirrordisplay.data.manager.QuoteManager
import com.elijahverdoorn.mirrordisplay.data.manager.WeatherManager
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class ComponentManager(
    val prefsManager: PrefsManager,
    val context: Context,
    val coroutineContext: CoroutineContext
) {

    @ExperimentalTime
    fun getBibleComponent(): BibleComponent {
        return makeBibleComponent(prefsManager.getDuration(context.getString(R.string.SHARED_PREFS_BIBLE_DURATION)))
    }

    @kotlin.time.ExperimentalTime
    private fun makeBibleComponent(duration: Duration) =
        BibleComponent(context).apply {
            setManager(BibleManager(duration))
        }

    @ExperimentalTime
    fun getWeatherComponent(): WeatherComponent {
        return makeWeatherComponent(
            prefsManager.prefs.getString(context.getString(R.string.SHARED_PREFS_WEATHER_API_KEY), "")!!,
            prefsManager.prefs.getFloat(context.getString(R.string.SHARED_PREFS_WEATHER_LAT), 0f),
            prefsManager.prefs.getFloat(context.getString(R.string.SHARED_PREFS_WEATHER_LON), 0f)
        )
    }

    @ExperimentalTime
    private fun makeWeatherComponent(
        apiKey: String,
        lat: Float,
        lon: Float
    ): WeatherComponent {
        val weatherComponent = WeatherComponent(context)
        val weatherManager = WeatherManager(coroutineContext, apiKey, lat, lon, duration = FullscreenActivity.ONE_HOUR)
        weatherComponent.setManager(weatherManager)
        return weatherComponent
    }

    fun getTimeComponent(): TimeComponent {
        val timeComponent = TimeComponent(context)
        return timeComponent
    }

    @ExperimentalTime
    fun getQuoteComponent(): QuoteComponent {
        return makeQuoteComponent(
            prefsManager.prefs.getString(context.getString(R.string.SHARED_PREFS_QUOTE_URL), "")!!,
            prefsManager.getDuration(context.getString(R.string.SHARED_PREFS_QUOTE_DURATION))
        )
    }

    @kotlin.time.ExperimentalTime
    private fun makeQuoteComponent(quoteUrl: String, duration: Duration) =
        QuoteComponent(context).apply {
            setManager(QuoteManager(quoteUrl, duration))
        }
}