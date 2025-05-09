package com.example.subway.api

import com.example.subway.model.SubwayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SubwayApi {
    @GET("{apiKey}/json/realtimeStationArrival/0/5/{stationName}")
    suspend fun getArrivalInfo(
        @Path("stationName") stationName: String,
        @Path("apiKey") apiKey: String = "654d784e55726c6138346873737853"
    ): Response<SubwayResponse>
}
