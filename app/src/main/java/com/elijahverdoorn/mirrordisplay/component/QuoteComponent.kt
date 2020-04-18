package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.QuoteManager
import kotlinx.android.synthetic.main.quote_component.view.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class QuoteComponent : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.quote_component, this)
    }

    suspend fun update(quoteManager: QuoteManager) {
        while (true) {
            val q = quoteManager.getQuote()
            quote.text = q.quote
            speaker.text = q.speaker
            delay(TimeUnit.SECONDS.toMillis(10)) // Temp: every 10s, run this
        }
    }
}
