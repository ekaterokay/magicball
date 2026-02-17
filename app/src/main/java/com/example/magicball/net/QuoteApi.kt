package com.example.magicball.net

import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun randomQuote(): QuoteResponse
}
