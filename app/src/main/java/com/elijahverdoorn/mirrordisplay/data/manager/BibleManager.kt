package com.elijahverdoorn.mirrordisplay.data.manager

import com.elijahverdoorn.mirrordisplay.data.model.Bible
import com.elijahverdoorn.mirrordisplay.data.model.Quote
import com.elijahverdoorn.mirrordisplay.data.source.RemoteBibleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BibleManager(
    override val coroutineContext: CoroutineContext = Dispatchers.IO
): CoroutineScope {
    val service = RemoteBibleService()

    // Return a single randome bible verse
    suspend fun getVerse() = withContext(coroutineContext) {
        service.fetchBible()
    }

}
