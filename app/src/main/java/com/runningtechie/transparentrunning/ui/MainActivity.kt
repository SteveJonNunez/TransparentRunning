package com.runningtechie.transparentrunning.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.runningtechie.transparentrunning.PermissionTool
import com.runningtechie.transparentrunning.R
import com.runningtechy.core.util.Activities
import com.runningtechy.core.util.intentTo
import com.runningtechy.database.TransparentRunningRepository
import com.runningtechy.database.model.WorkoutSession
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val WORKOUT_SESSION_CREATED: Int = 0
    }

    private lateinit var handlerThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private lateinit var uiHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createHandlerThread()
        createBackgroundHandler()
        createUiHandler()

        setupStartButton()
        setupStopButton()
        setupViewWorkoutsButton()

        val dataSet = mutableListOf<com.runningtechy.graphview.DataPoint>()

        dataSet.add(com.runningtechy.graphview.DataPoint(0, 1))
        dataSet.add(com.runningtechy.graphview.DataPoint(1, 1))
        dataSet.add(com.runningtechy.graphview.DataPoint(2, 2))
        dataSet.add(com.runningtechy.graphview.DataPoint(3, 1))
        dataSet.add(com.runningtechy.graphview.DataPoint(4, 4))
        dataSet.add(com.runningtechy.graphview.DataPoint(5, 4))
        dataSet.add(com.runningtechy.graphview.DataPoint(6, 2))
        dataSet.add(com.runningtechy.graphview.DataPoint(7, 4))
        dataSet.add(com.runningtechy.graphview.DataPoint(8, 5))
        dataSet.add(com.runningtechy.graphview.DataPoint(9, 1))
        dataSet.add(com.runningtechy.graphview.DataPoint(10, 5))

        graph_view.test(dataSet)
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionTool.FINE_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    startWorkout()
            }
        }
    }

    private fun setupStopButton() {
        stopButton.setOnClickListener {
            val intent = intentTo(Activities.GPSForegroundService, "ACTION_STOP_FOREGROUND_SERVICE", this@MainActivity)
            ContextCompat.startForegroundService(this@MainActivity, intent)
        }
    }

    private fun setupViewWorkoutsButton() {
        viewWorkoutsButton.setOnClickListener {
            startActivity(WorkoutSessionActivity.newIntent(this@MainActivity))
        }
    }

    private fun setupStartButton() {
        startButton.setOnClickListener {
            if (PermissionTool.hasFineLocation(this))
                startWorkout()
            else
                PermissionTool.requestFineLocation(this)
        }
    }


    private fun createUiHandler() {
        uiHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                super.handleMessage(message)
                if (message != null) {
                    when (message.what) {
                        WORKOUT_SESSION_CREATED -> {
                            val intent = intentTo(Activities.GPSForegroundService, "ACTION_START_FOREGROUND_SERVICE", this@MainActivity)
                            intent.putExtra("WORKOUT_SESSION_ID_KEY", message.obj as Long)
                            ContextCompat.startForegroundService(this@MainActivity, intent)
                        }
                    }
                }
            }
        }
    }

    private fun createBackgroundHandler() {
        backgroundHandler = Handler(handlerThread.looper)
    }

    private fun createHandlerThread() {
        handlerThread = HandlerThread("backgroundThread")
        handlerThread.start()
    }

    private fun startWorkout() {
        backgroundHandler.post {
            val workoutSessionId = TransparentRunningRepository.insertWorkoutSession(
                WorkoutSession(
                    title = "",
                    date = Date()
                )
            )
            val message = uiHandler.obtainMessage()
            message.what =
                WORKOUT_SESSION_CREATED
            message.obj = workoutSessionId
            uiHandler.sendMessage(message)
        }
    }
}

