package com.runningtechie.transparentrunning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)


        startButton.setOnClickListener {
            intent = Intent(this, GPSForegroundService::class.java)
            intent.action = ACTION_START_GPS_FOREGROUND_SERVICE
            ContextCompat.startForegroundService(this, intent)
        }

        stopButton.setOnClickListener {
            intent = Intent(this, GPSForegroundService::class.java)
            intent.action = ACTION_STOP_GPS_FOREGROUND_SERVICE
            ContextCompat.startForegroundService(this, intent)
        }
    }
}

