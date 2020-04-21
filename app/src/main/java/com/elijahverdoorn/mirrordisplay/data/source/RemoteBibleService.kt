package com.elijahverdoorn.mirrordisplay.data.source

import com.elijahverdoorn.mirrordisplay.data.model.Bible
import com.elijahverdoorn.mirrordisplay.service.RetrofitService
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Request


class RemoteBibleService {
    fun fetchBible(): Bible {
        val s = RetrofitService.client.newCall(Request.Builder().url(BIBLE_API_URL).build()).execute()
        return Json(JsonConfiguration.Default).parse(Bible.serializer().list, s.body?.string()?:"").first()
    }

    companion object {
        private const val BIBLE_API_URL = "http://labs.bible.org/api/?passage=random&type=json"
    }
}
