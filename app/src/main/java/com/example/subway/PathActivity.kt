package com.example.subway

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.subway.util.findOptimalTransferPath
import com.example.subway.util.loadSubwayGraph

class PathActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)

        val startEditText = findViewById<EditText>(R.id.stationEditText)
        val endEditText = findViewById<EditText>(R.id.lineEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        searchButton.setOnClickListener {
            val startStation = startEditText.text.toString().trim()
            val endStation = endEditText.text.toString().trim()

            if (startStation.isEmpty() || endStation.isEmpty()) {
                resultTextView.text = "🚩 출발역과 도착역을 모두 입력해주세요."
                return@setOnClickListener
            }

            val graph = loadSubwayGraph(this)
            val path = findOptimalTransferPath(graph, startStation, endStation)

            if (!path.isNullOrEmpty()) {
                val sb = StringBuilder()
                sb.append("🚇 최소 환승 경로:\n\n")

                for (i in path.indices) {
                    val (station, line) = path[i]
                    sb.append("$line $station\n")
                    if (i < path.size - 1) {
                        val (_, nextLine) = path[i + 1]
                        if (line != nextLine) {
                            sb.append("↓ (환승: $line → $nextLine)\n")
                        } else {
                            sb.append("↓\n")
                        }
                    }
                }
                resultTextView.text = sb.toString()
            } else {
                resultTextView.text = "❌ 경로를 찾을 수 없습니다."
            }
        }
    }
}
