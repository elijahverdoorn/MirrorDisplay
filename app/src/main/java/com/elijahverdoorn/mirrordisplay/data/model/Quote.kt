package com.elijahverdoorn.mirrordisplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val quote: String,
    val speaker: String
)

@Serializable
data class QuoteResponse(
    val quotes: List<Quote>
)
