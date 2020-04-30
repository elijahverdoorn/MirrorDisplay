package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Quote
import com.elijahverdoorn.mirrordisplay.data.source.RemoteQuoteService
import kotlinx.coroutines.*
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
        if (this::quotes.isInitialized && quotes.isEmpty().not()) {
            return popRandomQuote()
        }
        // quotes empty or not init
        fetchQuotes()
        return getQuote()
    }

    private suspend fun fetchQuotes() =
        withContext(this.coroutineContext) {
            quotes = service.fetchQuotes().quotes
        }

    private fun popRandomQuote(): Quote {
        val q = quotes.random()
        quotes = quotes.filterNot { it == q }
        return q
    }
}
