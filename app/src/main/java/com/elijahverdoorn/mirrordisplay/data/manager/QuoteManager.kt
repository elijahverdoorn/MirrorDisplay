package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Quote
import com.elijahverdoorn.mirrordisplay.data.source.RemoteQuoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuoteManager(
    url: String,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
): CoroutineScope {
    val service = RemoteQuoteService(url)
    lateinit var quotes: List<Quote>

    init {
        launch {
            fetchQuotes()
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
        fetchQuotes()
        return getQuote()
    }

    private suspend fun fetchQuotes() =
        withContext(coroutineContext) {
            quotes = service.fetchQuotes().quotes
        }
}
