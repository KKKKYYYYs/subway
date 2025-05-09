package com.example.subway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subway.api.RetrofitInstance
import com.example.subway.model.ArrivalInfo
import com.example.subway.model.SubwayResponse
import kotlinx.coroutines.launch

class SubwayViewModel : ViewModel() {

    private val _subwayData = MutableLiveData<List<ArrivalInfo>>() // List로 변경
    val subwayData: LiveData<List<ArrivalInfo>> = _subwayData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // 노선 필터를 적용한 fetch 함수
    fun fetchSubwayInfo(stationName: String, lineFilter: String? = null) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getArrivalInfo(stationName)
                if (response.isSuccessful && response.body() != null) {
                    val arrivalList = response.body()!!.realtimeArrivalList

                    // 필터링 조건이 있을 경우 적용
                    val filteredList = if (!lineFilter.isNullOrBlank()) {
                        arrivalList.filter { it.trainLine.contains(lineFilter) }
                    } else {
                        arrivalList
                    }

                    _subwayData.value = filteredList
                } else {
                    _errorMessage.value = "데이터를 불러오지 못했습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류: ${e.message}"
            }
        }
    }
}
