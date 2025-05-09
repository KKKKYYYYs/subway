package com.example.subway.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.subway.model.SubwayResponse

interface SubwayApiService {
    @GET("api/subway/{apiKey}/json/realtimeStationArrival/0/5/{stationName}")
    fun getRealtimeArrival(
        @Path("apiKey") apiKey: String,
        @Path("stationName") stationName: String
    ): Call<SubwayResponse>
}