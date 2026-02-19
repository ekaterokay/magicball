package com.example.magicball.data

import com.example.magicball.net.AdviceApiClient

class AdviceRepository {
    suspend fun fetchAdvice(): String {
        return AdviceApiClient.api.getAdvice().slip.advice
    }
}
