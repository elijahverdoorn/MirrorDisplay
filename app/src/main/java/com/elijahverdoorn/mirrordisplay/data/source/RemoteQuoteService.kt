package com.elijahverdoorn.mirrordisplay.data.source

import com.elijahverdoorn.mirrordisplay.data.model.QuoteResponse
import com.elijahverdoorn.mirrordisplay.service.RetrofitService
import retrofit2.http.GET

interface RemoteQuoteService {
    @GET("/quotes.json")
    suspend fun fetchQuotes(): QuoteResponse

    companion object {
        fun create(url: String) = RetrofitService.create(RemoteQuoteService::class.java, url)

    }
}
