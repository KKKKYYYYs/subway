package com.example.subway

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.subway.util.loadSubwayGraph

class LineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line)

        val lineEditText = findViewById<EditText>(R.id.lineText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        searchButton.setOnClickListener {
            val line = lineEditText.text.toString().trim()

            if (line.isEmpty()) {
                resultTextView.text = "🚇 호선을 입력해주세요 (예: 2호선)"
                return@setOnClickListener
            }

            val graph = loadSubwayGraph(this)
            val stations = graph.keys
                .filter { it.endsWith("_$line") }
                .map { it.substringBefore("_") }
                .distinct()
                .sorted()

            if (stations.isNotEmpty()) {
                resultTextView.text = "🚇 $line 역 목록:\n\n" + stations.joinToString(" → ")
            } else {
                resultTextView.text = "❌ 해당 호선에 대한 정보를 찾을 수 없습니다."
            }
        }
    }
}
