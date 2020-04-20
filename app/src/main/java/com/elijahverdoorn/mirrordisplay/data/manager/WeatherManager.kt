package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.WeatherResponse
import com.elijahverdoorn.mirrordisplay.data.source.RemoteWeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WeatherManager(
    override val coroutineContext: CoroutineContext,
    val apiKey: String
): CoroutineScope {

    private val service = RemoteWeatherService.create()
    private lateinit var weather: WeatherResponse
    var lat: Float? = null
    var lon: Float? = null

    constructor(
        coroutineContext: CoroutineContext,
        apiKey: String,
        lat: Float,
        lon: Float
    ): this(coroutineContext, apiKey) {
        this.lat = lat
        this.lon = lon
        launch {
            weather = getWeather(lat, lon)
        }
    }

    suspend fun getWeather(): WeatherResponse {
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
