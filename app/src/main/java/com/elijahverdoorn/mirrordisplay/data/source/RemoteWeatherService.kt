package com.elijahverdoorn.mirrordisplay.data.source

import com.elijahverdoorn.mirrordisplay.data.model.WeatherResponse
import com.elijahverdoorn.mirrordisplay.service.RetrofitService
import retrofit2.http.GET

interface RemoteWeatherService {
    @GET("onecall?lat=${OAKLAND_LAT}&lon=${OAKLAND_LON}&appid=${WEATHER_API_KEY}&units=imperial")
    suspend fun fetchWeather(): WeatherResponse

    companion object {
        fun create() = RetrofitService.create(RemoteWeatherService::class.java, WEATHER_BASE_URL)

        private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val WEATHER_API_KEY = "028d7ee980bd54533953095ee6ddbf61"

        private const val OAKLAND_LAT = "37.8044"
        private const val OAKLAND_LON = "122.2712"
    }
}
