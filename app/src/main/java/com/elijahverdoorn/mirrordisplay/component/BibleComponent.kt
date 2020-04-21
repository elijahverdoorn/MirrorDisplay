package com.elijahverdoorn.mirrordisplay.component

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.elijahverdoorn.mirrordisplay.R
import com.elijahverdoorn.mirrordisplay.data.manager.BibleManager
import kotlinx.android.synthetic.main.bible_component.view.*
import kotlinx.coroutines.delay
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
            val q = bibleManager.getVerse()
            verse.text = q.text
            chapter.text = resources.getString(R.string.chapter_template, q.bookname, q.chapter, q.verse)
            delay(TimeUnit.SECONDS.toMillis(10)) // Temp: every 10s, run this
        }
    }
}
