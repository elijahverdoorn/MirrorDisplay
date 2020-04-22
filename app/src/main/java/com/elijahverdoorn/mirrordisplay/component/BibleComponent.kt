package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.BibleManager
import kotlinx.android.synthetic.main.bible_component.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class BibleComponent : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.bible_component, this)
    }

    suspend fun update(bibleManager: BibleManager) {
        while (true) {
            fadeIn()
            delay(TimeUnit.SECONDS.toMillis(10)) // Temp: every 10s, run this
            fadeOut()
            val v = bibleManager.getVerse()
            verse.text = Html.fromHtml(v.text)
            chapter.text = resources.getString(R.string.chapter_template, v.bookname, v.chapter, v.verse)
        }
    }

    private suspend fun fadeIn() {
        verse.startAnimation(loadAnimation(context, R.anim.fade_in))
        chapter.startAnimation(loadAnimation(context, R.anim.fade_in))
        verse.alpha = 1f
        chapter.alpha = 1f
        delay(1000)
    }

    private suspend fun fadeOut() {
        verse.startAnimation(loadAnimation(context, R.anim.fade_out))
        chapter.startAnimation(loadAnimation(context, R.anim.fade_out))
        delay(1000)
        verse.alpha = 0f
        chapter.alpha = 0f
    }
}
