package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Bible
import com.elijahverdoorn.mirrordisplay.data.source.RemoteBibleService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

@ExperimentalTime
class BibleManager(
    interval: Duration,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
): CoroutineScope {
    val service = RemoteBibleService()
    val bibleChannel = Channel<Bible>()

    init {
        launch {
            while (true) {
                bibleChannel.send(getVerse())
                delay(interval)
            }
        }
    }

    // Return a single random bible verse
    private suspend fun getVerse() = withContext(coroutineContext) {
        service.fetchBible()
    }

}
