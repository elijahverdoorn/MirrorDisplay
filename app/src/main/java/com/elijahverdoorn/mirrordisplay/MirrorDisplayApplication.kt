package com.elijahverdoorn.mirrordisplay

import android.app.Application
import com.elijahverdoorn.mirrordisplay.di.mirrorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MirrorDisplayApplication : Application() {
    init {
        startKoin {
            androidContext(this@MirrorDisplayApplication)
            modules(mirrorModule)
        }
    }
}
