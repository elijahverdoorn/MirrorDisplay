package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.elijahverdoorn.mirrordisplay.R
import kotlinx.android.synthetic.main.time_component.view.*
import kotlinx.coroutines.delay
import java.util.*

class TimeComponent: FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.time_component, this)
    }

    suspend fun update() {
        while (true) {
            val calendar = Calendar.getInstance()
            val s = calendar.get(Calendar.HOUR).toString() + ":" +
                    if (calendar.get(Calendar.MINUTE) < 10) {
                        "0"
                    } else {
                        ""
                    } + calendar.get(Calendar.MINUTE).toString()
            time.text = s
            label.text = if (calendar.get(Calendar.AM_PM) == 0) "AM" else "PM"
            delay(1000)
        }
    }
}
