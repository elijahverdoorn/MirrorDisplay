package com.elijahverdoorn.mirrordisplay.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitService {
    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    val client = OkHttpClient.Builder()
        .cache(null)
        .addInterceptor(logger)
        .build()
    private const val MEDIA_TYPE_JSON = "application/json"

    fun <S> create(clazz: Class<S>, url: String) =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(Json.nonstrict.asConverterFactory(MEDIA_TYPE_JSON.toMediaType())) // Nonstrict so we don't have to create complete models
            .client(client)
            .build()
            .create(clazz)

}
