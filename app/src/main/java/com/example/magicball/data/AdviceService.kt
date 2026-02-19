package com.example.magicball.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdviceService {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        // ВАЖНО: Forismatic часто на http. Если хочешь https — сначала проверь, что работает.
        .baseUrl("http://api.forismatic.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: ForismaticApi = retrofit.create(ForismaticApi::class.java)
}
