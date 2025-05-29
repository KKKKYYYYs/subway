package com.example.subway.repository

import com.example.subway.api.RetrofitClient
import com.example.subway.model.ArrivalInfo

class SubwayRepository {

    private val apiKey = "654d784e55726c6138346873737853"

    suspend fun getArrivalInfo(stationName: String): List<ArrivalInfo> {
        val response = RetrofitClient.api.getArrivalInfo(apiKey, stationName)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.realtimeArrivalList
        } else {
            throw Exception("응답 실패: ${response.code()} ${response.message()}")
        }
    }
}
