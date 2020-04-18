package com.elijahverdoorn.mirrordisplay.data.source

import com.elijahverdoorn.mirrordisplay.data.model.QuoteResponse
import com.elijahverdoorn.mirrordisplay.service.RetrofitService
import retrofit2.http.GET

interface RemoteQuoteService {
    @GET("/quotes.json")
    suspend fun fetchQuotes(): QuoteResponse

    companion object {
        fun create() = RetrofitService.create(RemoteQuoteService::class.java, QUOTE_BASE_URL)

        private const val QUOTE_BASE_URL = "http://10.0.2.2:4000/"
    }
}
