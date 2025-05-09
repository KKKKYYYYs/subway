package com.example.subway.model

import com.google.gson.annotations.SerializedName

data class SubwayResponse(
    @SerializedName("realtimeArrivalList") val realtimeArrivalList: List<ArrivalInfo>
)

data class ArrivalInfo(
    @SerializedName("statnNm") val stationName: String,
    @SerializedName("trainLineNm") val trainLine: String,
    @SerializedName("arvlMsg2") val arrivalMessage: String
)