package com.example.subway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subway.model.ArrivalInfo
import com.example.subway.repository.SubwayRepository
import kotlinx.coroutines.launch

class SubwayViewModel : ViewModel() {

    private val repository = SubwayRepository()

    private val _subwayData = MutableLiveData<List<ArrivalInfo>>()
    val subwayData: LiveData<List<ArrivalInfo>> = _subwayData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchSubwayInfo(stationName: String, lineFilter: String? = null) {
        viewModelScope.launch {
            try {
                val list = repository.getArrivalInfo(stationName)
                // 라인 필터가 있으면 필터링
                val filtered = if (lineFilter.isNullOrEmpty()) list
                else list.filter { it.trainLine.contains(lineFilter) }
                _subwayData.value = filtered
            } catch (e: Exception) {
                _errorMessage.value = "오류 발생: ${e.message}"
            }
        }
    }
}
