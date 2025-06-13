package com.example.subway

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.subway.viewmodel.SubwayViewModel

class InfoActivity : AppCompatActivity() {

    private val viewModel: SubwayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val refreshButton = findViewById<Button>(R.id.refreshButton)

        searchButton.setOnClickListener {
            val stationName = searchEditText.text.toString().trim()


            if (stationName.isNotEmpty()) {
                viewModel.fetchSubwayInfo(stationName, null)
            } else {
                resultTextView.text = "🔍 역 이름을 입력해주세요."
            }
        }
        refreshButton.setOnClickListener {
            val stationName = searchEditText.text.toString().trim()
             // 필요 시 사용

            if (stationName.isNotEmpty()) {
                viewModel.fetchSubwayInfo(stationName, null)
            } else {
                resultTextView.text = "🔁 새로고침하려면 먼저 역 이름을 입력해주세요."
            }
        }

        viewModel.subwayData.observe(this, Observer { list ->
            // list가 null일 경우 빈 리스트로 처리
            val safeList = list ?: emptyList()

            if (safeList.isNotEmpty()) {
                val result = safeList.joinToString("\n\n") {
                    "🚇 ${it.stationName}역 - ${it.trainLine} - ${it.arrivalMessage}"
                }
                resultTextView.text = result
            } else {
                resultTextView.text = "🚫 도착 정보를 찾을 수 없습니다."
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            resultTextView.text = "❌ 오류: $it"
        })



    }
    }

