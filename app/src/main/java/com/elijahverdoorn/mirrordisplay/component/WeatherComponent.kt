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
import com.elijahverdoorn.mirrordisplay.data.manager.DataManager
import com.elijahverdoorn.mirrordisplay.data.manager.WeatherManager
import kotlinx.android.synthetic.main.weather_component.view.*

class WeatherComponent : FrameLayout, Component {
    private lateinit var manager: DataManager

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.weather_component, this)
    }

    override fun setManager(dataManager: DataManager) {
        manager = dataManager
    }

    override suspend fun update() {
        for (w in (manager as WeatherManager).weatherChannel) {
            currentTemp.text = resources.getString(R.string.temp_f_template, w.current.temp)
            tomorrowTemp.text = resources.getString(R.string.temp_f_template, w.daily.first().temp.day)

            loadWeatherIcon(currentTemp, w.current.weather.first().iconUrl)
            loadWeatherIcon(tomorrowTemp, w.daily.get(1).weather.first().iconUrl)
        }
    }

    private fun loadWeatherIcon(view: TextView, url: String) {
        Glide.with(context)
            .load(url)
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
