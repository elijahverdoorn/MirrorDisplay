package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.QuoteManager
import kotlinx.android.synthetic.main.quote_component.view.*
import kotlinx.coroutines.delay

class QuoteComponent : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.quote_component, this)
    }

    @kotlin.time.ExperimentalTime
    suspend fun update(quoteManager: QuoteManager) {
        for (q in quoteManager.quotesChannel) {
            fadeOut()
            quote.text = resources.getString(R.string.quote_template, q.quote)
            speaker.text = resources.getString(R.string.quote_speaker_template, q.speaker)
            fadeIn()
        }
    }

    private suspend fun fadeIn() {
        quote.startAnimation(loadAnimation(context, R.anim.fade_in))
        speaker.startAnimation(loadAnimation(context, R.anim.fade_in))
        quote.alpha = 1f
        speaker.alpha = 1f
        delay(1000)
    }

    private suspend fun fadeOut() {
        quote.startAnimation(loadAnimation(context, R.anim.fade_out))
        speaker.startAnimation(loadAnimation(context, R.anim.fade_out))
        delay(1000)
        quote.alpha = 0f
        speaker.alpha = 0f
    }
}
