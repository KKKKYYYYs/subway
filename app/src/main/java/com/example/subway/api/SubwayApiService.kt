// SubwayApiService.kt
package com.example.subway.api

import com.example.subway.model.SubwayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SubwayApiService {
    @GET("subway/{apiKey}/json/realtimeStationArrival/0/5/{stationName}")
    suspend fun getArrivalInfo(
        @Path("apiKey") apiKey: String,
        @Path("stationName") stationName: String
    ): Response<SubwayResponse>
}
