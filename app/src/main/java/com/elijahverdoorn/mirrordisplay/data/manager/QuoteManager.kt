package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Quote
import com.elijahverdoorn.mirrordisplay.data.source.RemoteQuoteService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class QuoteManager(
    url: String,
    interval: Duration,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
): CoroutineScope {
    val service = RemoteQuoteService(url)
    private lateinit var quotes: List<Quote>
    val quotesChannel = Channel<Quote>()

    init {
        launch {
            fetchQuotes()
            while (true) {
                quotesChannel.send(getQuote())
                delay(interval)
            }
        }
    }

    // Return a single Quote
    private suspend fun getQuote(): Quote {
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
