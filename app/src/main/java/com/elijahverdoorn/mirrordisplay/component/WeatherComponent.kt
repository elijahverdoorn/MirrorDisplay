package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.WeatherManager
import kotlinx.android.synthetic.main.weather_component.view.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class WeatherComponent : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.weather_component, this)
    }

    suspend fun update(weatherManager: WeatherManager) {
        while (true) {
            val w = weatherManager.getWeather()
            currentTemp.text = w.current.temp.toString() + " F"
            tomorrowTemp.text = w.daily.get(1).temp.day.toString() + " F"

            loadWeatherIcon(currentTemp, w.current.weather.first().iconUrl)
            loadWeatherIcon(tomorrowTemp, w.daily.get(1).weather.first().iconUrl)

            delay(TimeUnit.MINUTES.toMillis(1))
        }
    }

    private fun loadWeatherIcon(view: TextView, url: String) {
        Glide.with(context)
            .load(url)
            // TODO: Add placeholder
//                .placeholder()
//                .error()
            .into(object : CustomTarget<Drawable>(100, 100) {
                override fun onLoadCleared(drawable: Drawable?) {
                    view.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                }

                override fun onResourceReady(
                    res: Drawable,
                    transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                ) {
                    view.setCompoundDrawablesWithIntrinsicBounds(res, null, null, null)
                }
            })
    }
}
