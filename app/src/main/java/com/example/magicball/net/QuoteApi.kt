package com.example.magicball.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApi {

    @GET("random")
    suspend fun randomQuote(): QuoteResponse

    companion object {
        fun create(): QuoteApi {
            return Retrofit.Builder()
                .baseUrl("https://api.quotable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuoteApi::class.java)
        }
    }
}
