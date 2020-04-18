package com.elijahverdoorn.mirrordisplay.data.model

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// NOTE: Incomplete models
@Serializable
data class Weather(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    @ContextualSerialization private val fetchTime: LocalDateTime = LocalDateTime.now()
) {

    val stale: Boolean
        get() = LocalDateTime.now().isAfter(fetchTime.plus(15, ChronoUnit.MINUTES))


    @Serializable
    data class Current(
        val temp: Float
    )

    @Serializable
    data class Hourly(
        val temp: Float
    )

    @Serializable
    data class Daily(
        val temp: Temp
    ) {
        @Serializable
        data class Temp(
            val day: Float,
            val min: Float,
            val max: Float,
            val night: Float,
            val eve: Float,
            val morn: Float
        )
    }
}
