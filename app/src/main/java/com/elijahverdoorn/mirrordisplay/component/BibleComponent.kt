package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.BibleManager
import com.elijahverdoorn.mirrordisplay.data.manager.DataManager
import kotlinx.android.synthetic.main.bible_component.view.*
import kotlinx.coroutines.delay
import kotlin.time.Duration

class BibleComponent : FrameLayout, Component {
    private lateinit var manager: DataManager

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.bible_component, this)
    }

    override fun setManager(dataManager: DataManager) {
        manager = dataManager
    }

    @kotlin.time.ExperimentalTime
    override suspend fun update() {
        for (b in (manager as BibleManager).bibleChannel) {
            fadeOut()
            verse.text = Html.fromHtml(b.text)
            chapter.text = resources.getString(R.string.chapter_template, b.bookname, b.chapter, b.verse)
            fadeIn()
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
