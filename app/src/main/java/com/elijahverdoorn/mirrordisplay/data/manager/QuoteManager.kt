package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Quote
import com.elijahverdoorn.mirrordisplay.data.source.RemoteQuoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class QuoteManager(override val coroutineContext: CoroutineContext): CoroutineScope {

    val service = RemoteQuoteService.create()
    lateinit var quotes: List<Quote>

    init {
        launch {
            quotes = fetchQuotes()
        }
    }

    // Return a single Quote
    suspend fun getQuote(): Quote {
        if (this::quotes.isInitialized && !quotes.isEmpty()) {
            val q = quotes.random()
            quotes = quotes.filterNot { it == q }
            return q
        }
        // quotes empty or not init
        quotes = fetchQuotes()
        return getQuote()
    }

    private suspend fun fetchQuotes() = service.fetchQuotes().quotes

}
