package com.example.subway

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.subway.viewmodel.SubwayViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: SubwayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stationEditText = findViewById<EditText>(R.id.stationEditText)
        val lineEditText = findViewById<EditText>(R.id.lineEditText) // 추가된 호선 입력 EditText
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        resultTextView.movementMethod = android.text.method.ScrollingMovementMethod()

        // 버튼 클릭 시 ViewModel에 요청
        searchButton.setOnClickListener {
            val stationName = stationEditText.text.toString().trim()
            val lineFilter = lineEditText.text.toString().trim() // 호선 정보 입력받기

            if (stationName.isNotEmpty()) {
                // 호선이 입력되지 않았으면 null로 전달
                viewModel.fetchSubwayInfo(stationName, if (lineFilter.isEmpty()) null else lineFilter)
            } else {
                resultTextView.text = "역 이름을 입력해주세요."
            }
        }

        // **여기** 필터링된 데이터를 관찰하는 부분 추가
        viewModel.filteredData.observe(this, Observer { list ->
            val builder = StringBuilder()
            list.forEach {
                builder.append("${it.trainLine} / ${it.arrivalMessage}\n")
            }
            resultTextView.text = builder.toString()
        })

        // 도착 정보 관찰
        viewModel.subwayData.observe(this, Observer { response ->
            val builder = StringBuilder()
            response.forEach {
                builder.append("🚇 ${it.stationName}역 - ${it.trainLine} - ${it.arrivalMessage}\n\n")
            }
            resultTextView.text = builder.toString()
        })

        // 에러 메시지 관찰
        viewModel.errorMessage.observe(this, Observer { error ->
            resultTextView.text = error
        })
    }
}
