package com.example.subway

import android.os.Bundle
import android.widget.*
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
        val lineEditText = findViewById<EditText>(R.id.lineEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        searchButton.setOnClickListener {
            val stationName = stationEditText.text.toString().trim()
            val lineFilter = lineEditText.text.toString().trim()

            if (stationName.isNotEmpty()) {
                viewModel.fetchSubwayInfo(stationName, if (lineFilter.isEmpty()) null else lineFilter)
            } else {
                resultTextView.text = "역 이름을 입력해주세요. "
            }
        }

        viewModel.subwayData.observe(this, Observer { list ->
            val result = list.joinToString("\n\n") {
                "🚇 ${it.stationName}역 - ${it.trainLine} - ${it.arrivalMessage}"
            }
            resultTextView.text = result
        })

        viewModel.errorMessage.observe(this, Observer {
            resultTextView.text = it
        })
    }
}
