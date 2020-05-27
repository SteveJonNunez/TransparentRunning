package com.runningtechie.transparentrunning.ui

import android.content.pm.PackageManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.runningtechie.transparentrunning.GPSForegroundService
import com.runningtechie.transparentrunning.PermissionTool
import com.runningtechie.transparentrunning.R
import com.runningtechie.transparentrunning.database.TransparentRunningRepository
import com.runningtechie.transparentrunning.model.WorkoutSession
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date


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
            GPSForegroundService.stopGpsForegroundService(
                this
            )
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
            override fun handleMessage(message: Message?) {
                super.handleMessage(message)
                if (message != null) {
                    when (message.what) {
                        WORKOUT_SESSION_CREATED -> GPSForegroundService.startGpsForegroundService(
                            this@MainActivity,
                            message.obj as Long
                        )
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

