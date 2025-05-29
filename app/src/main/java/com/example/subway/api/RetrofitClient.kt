// RetrofitClient.kt
package com.example.subway.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://swopenapi.seoul.go.kr/api/"

    val api: SubwayApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SubwayApiService::class.java)
    }
}
