package com.example.magicball.data.api

import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}
