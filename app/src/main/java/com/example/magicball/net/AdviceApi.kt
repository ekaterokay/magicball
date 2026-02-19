package com.example.magicball.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}

object AdviceApiClient {
    private const val BASE_URL = "https://api.adviceslip.com/"

    val api: AdviceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdviceApi::class.java)
    }
}
