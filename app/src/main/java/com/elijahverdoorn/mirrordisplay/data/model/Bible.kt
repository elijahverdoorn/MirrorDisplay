package com.elijahverdoorn.mirrordisplay.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Bible(
    val bookname: String,
    val chapter: String,
    val verse: String,
    val text: String
)
