package com.elijahverdoorn.mirrordisplay.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.elijahverdoorn.mirrordisplay.BuildConfig
import com.elijahverdoorn.mirrordisplay.R

object IntentUtil {
    fun getEmailDeveloperIntent(context: Context) = Intent(Intent.ACTION_SEND).apply {
        type = "*/*"
        putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.developer_email))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_footer_text, BuildConfig.VERSION_NAME, DeviceInfo.getDeviceInfo(), DeviceInfo.getAndroidVersion()) )
    }

    fun getBrowserIntent(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))
}