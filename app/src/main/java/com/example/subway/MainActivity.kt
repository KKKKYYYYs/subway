package com.example.subway


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.subway.InfoActivity
import com.example.subway.LineActivity
import com.example.subway.PathActivity
import com.example.subway.R

class MainActivity : AppCompatActivity() {
    var infoButton: Button? = null
    var pathButton: Button? = null
    var lineButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoButton = findViewById<Button?>(R.id.infoButton)
        pathButton = findViewById<Button?>(R.id.pathButton)
        lineButton = findViewById<Button?>(R.id.lineButton)

        // 정보조회 버튼 클릭
        infoButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, InfoActivity::class.java)
            startActivity(intent)
        })

        // 경로조회 버튼 클릭
        pathButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, PathActivity::class.java)
            startActivity(intent)
        })

        // 호선조회 버튼 클릭
        lineButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, LineActivity::class.java)
            startActivity(intent)
        })
    }
}