package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.WeatherResponse
import com.elijahverdoorn.mirrordisplay.data.source.RemoteWeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WeatherManager(override val coroutineContext: CoroutineContext): CoroutineScope {

    private val service = RemoteWeatherService.create()
    private lateinit var weather: WeatherResponse

    init {
        launch {
            weather = fetchWeather()
        }
    }

    suspend fun getWeather(): WeatherResponse {
        return if (this::weather.isInitialized && !weather.stale) {
            weather
        } else {
            return fetchWeather()
        }
    }

    private suspend fun fetchWeather() = service.fetchWeather()

}
