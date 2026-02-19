package com.example.magicball.data

import retrofit2.http.GET
import retrofit2.http.Query

data class ForismaticQuote(
    val quoteText: String? = null,
    val quoteAuthor: String? = null
)

interface ForismaticApi {
    @GET("api/1.0/")
    suspend fun getQuote(
        @Query("method") method: String = "getQuote",
        @Query("format") format: String = "json",
        @Query("lang") lang: String = "ru"
    ): ForismaticQuote
}
