package com.example.subway

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.subway.repository.SubwayRepository
import com.example.subway.util.findOptimalTransferPath
import com.example.subway.util.loadSubwayGraph
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.subway.viewmodel.SubwayViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer



class MainActivity : AppCompatActivity() {
    private val viewModel: SubwayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = SubwayRepository()

        val searchText = findViewById<EditText>(R.id.searchText)
        val stationEditText = findViewById<EditText>(R.id.stationEditText)
        val endEditText = findViewById<EditText>(R.id.lineEditText)
        val lineEditText = findViewById<EditText>(R.id.lineText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        searchButton.setOnClickListener {
            val stationName = searchText.text.toString().trim()
            val startStation = stationEditText.text.toString().trim()
            val endStation = endEditText.text.toString().trim()
            val lineFilter = lineEditText.text.toString().trim()
            val graph = loadSubwayGraph(this)

            // ✅ case 1: 호선만 입력 (출발/도착 비었을 때)
            if (startStation.isEmpty() && endStation.isEmpty() && lineFilter.isNotEmpty()) {
                val lineStations = graph.keys
                    .filter { it.endsWith("_$lineFilter") }
                    .map { it.replace("_$lineFilter", "") }
                    .distinct()
                    .sorted()

                resultTextView.text = if (lineStations.isNotEmpty()) {
                    "🚇 $lineFilter 노선 역 목록:\n\n" + lineStations.joinToString(" → ")
                } else {
                    "해당 호선의 역을 찾을 수 없습니다."
                }
                return@setOnClickListener
            }

            // ✅ case 2: 출발역 + 도착역 입력됨 (호선 입력은 선택사항)
            if (startStation.isNotEmpty() && endStation.isNotEmpty()) {
                val path = findOptimalTransferPath(graph, startStation, endStation)

                if (path != null && path.isNotEmpty()) {
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
                    resultTextView.text = "경로를 찾을 수 없습니다."
                }
                return@setOnClickListener
            }
            if (stationName.isNotEmpty()) {
                viewModel.fetchSubwayInfo(stationName, if (lineFilter.isEmpty()) null else lineFilter)
            } else {
                resultTextView.text = "🔍 역 이름을 입력해주세요."
            }
        }

        viewModel.subwayData.observe(this, Observer { list ->
            if (!list.isNullOrEmpty()) {
                val result = list.joinToString("\n\n") {
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

            // ⚠️ case 3: 아무것도 입력 안 됐을 때
            resultTextView.text = "출발역/도착역 또는 호선 중 하나 이상 입력해주세요."
        }
    }


