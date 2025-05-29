package com.example.subway.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://swopenapi.seoul.go.kr/api/subway/"

    val api: SubwayApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://swopenAPI.seoul.go.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SubwayApi::class.java)
    }
}