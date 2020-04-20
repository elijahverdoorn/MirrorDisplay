package com.elijahverdoorn.mirrordisplay.data.source

import com.elijahverdoorn.mirrordisplay.data.model.QuoteResponse
import com.elijahverdoorn.mirrordisplay.service.RetrofitService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Request

class RemoteQuoteService(
    val url: String
) {
    fun fetchQuotes(): QuoteResponse {
        val s = RetrofitService.client.newCall(Request.Builder().url(url).build()).execute()
        return Json(JsonConfiguration.Default).parse(QuoteResponse.serializer(), s.body?.string()?:"")
    }
}
