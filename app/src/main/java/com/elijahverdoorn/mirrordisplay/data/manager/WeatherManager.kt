package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Weather
import com.elijahverdoorn.mirrordisplay.data.model.WeatherResponse
import com.elijahverdoorn.mirrordisplay.data.source.RemoteWeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class WeatherManager @ExperimentalTime constructor(
    override val coroutineContext: CoroutineContext,
    val apiKey: String,
    val interval: Duration
): CoroutineScope {

    private val service = RemoteWeatherService.create()
    private lateinit var weather: WeatherResponse
    var lat: Float? = null
    var lon: Float? = null
    val weatherChannel = Channel<WeatherResponse>()

    @ExperimentalTime
    constructor(
        coroutineContext: CoroutineContext,
        apiKey: String,
        lat: Float,
        lon: Float,
        duration: Duration
    ): this(coroutineContext, apiKey, duration) {
        this.lat = lat
        this.lon = lon
        launch {
            weather = getWeather(lat, lon)
            while (true) {
                weatherChannel.send(getWeather())
                delay(duration)
            }
        }
    }

    private suspend fun getWeather(): WeatherResponse {
        requireNotNull(lat)
        requireNotNull(lon)
        return getWeather(lat!!, lon!!)
    }

    private suspend fun getWeather(lat: Float, lon: Float): WeatherResponse {
        return if (this::weather.isInitialized && !weather.stale) {
            weather
        } else {
            return service.fetchWeather(lat, lon, apiKey)
        }
    }
}
