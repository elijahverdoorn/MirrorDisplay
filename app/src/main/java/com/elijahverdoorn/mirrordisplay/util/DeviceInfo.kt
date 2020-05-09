package com.elijahverdoorn.mirrordisplay.util

import android.os.Build

object DeviceInfo {
    fun getDeviceInfo(): String {
        val manu = Build.MANUFACTURER
        val model = Build.MODEL
        return "Manufacturer: $manu, Model: $model"
    }

    fun getAndroidVersion(): String {
        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        return "Android SDK: $sdkVersion ($release)"
    }
}


