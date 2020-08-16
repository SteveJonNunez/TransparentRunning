package com.runningtechy.gpsForegroundService

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.runningtechie.transparentrunning.ui.MainActivity

const val CHANNEL_ID_STRING = "GPS_FOREGROUND_SERVICE"
const val CHANNEL_ID_INT = 1
const val ACTION_START_GPS_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
const val ACTION_STOP_GPS_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
const val ACTION_PAUSE_GPS_FOREGROUND_SERVICE = "ACTION_PAUSE"
const val ACTION_PLAY_GPS_FOREGROUND_SERVICE = "ACTION_PLAY"
const val ACTION_FINISH_GPS_FOREGROUND_SERVICE = "ACTION_FINISH"

const val WORKOUT_SESSION_ID_KEY = "WORKOUT_SESSION_ID_KEY"


class GPSForegroundService : Service() {
    private lateinit var gpsLocationProvider: GPSLocationProvider

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var workoutSessionId: Long = 0L

    private val pauseStartActionIndex = 0

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_GPS_FOREGROUND_SERVICE -> {
                if (intent.hasExtra(WORKOUT_SESSION_ID_KEY))
                    startForegroundService(intent.getLongExtra(WORKOUT_SESSION_ID_KEY, 0L))
                else
                    throw RuntimeException() //TODO: Give user error instead of throwing exception
            }
            ACTION_STOP_GPS_FOREGROUND_SERVICE -> stopForegroundService()
            ACTION_PLAY_GPS_FOREGROUND_SERVICE -> play()
            ACTION_PAUSE_GPS_FOREGROUND_SERVICE -> pause()
            ACTION_FINISH_GPS_FOREGROUND_SERVICE -> stopForegroundService()
            else -> throw UnsupportedOperationException("Unsupported intent action: $intent.action")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented.")
    }

    private fun createNotificationChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID_STRING, "My Background Service")
        } else {
            // If earlier than version 26, channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(notificationChannel)
        return channelId
    }

    @SuppressLint("RestrictedApi")
    private fun startForegroundService(workoutSessionId: Long) {
        this.workoutSessionId = workoutSessionId
        gpsLocationProvider =
            GPSLocationProvider(workoutSessionId, this)
        setupOngoingNotification()
        gpsLocationProvider.startOngoingLocationUpdates()
        startForeground(CHANNEL_ID_INT, notificationBuilder.build())
    }

    private fun setupOngoingNotification() {
        val channelId: String = createNotificationChannel()
        notificationBuilder = NotificationCompat.Builder(this, channelId)

        // Make notification show big text.
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Big Content title")
        bigTextStyle.bigText("Big Text.")
        // Set big text style.
        notificationBuilder.setStyle(bigTextStyle)
        notificationBuilder.setContentTitle("Content title")
        notificationBuilder.setContentText("Content Text")
        notificationBuilder.setSubText("Sub text")

        notificationBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon)
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        // Create notification default intent.
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        notificationBuilder.setFullScreenIntent(pendingIntent, true)

        // Add Pause button intent in notification.
        val pauseIntent = Intent(this, GPSForegroundService::class.java)
        pauseIntent.action =
            ACTION_PAUSE_GPS_FOREGROUND_SERVICE
        val pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val pauseAction =
            NotificationCompat.Action(android.R.drawable.ic_media_play, "Pause", pendingPauseIntent)
        notificationBuilder.addAction(pauseAction)

        // Add Finish button intent in notification.
        val finishIntent = Intent(this, GPSForegroundService::class.java)
        finishIntent.action =
            ACTION_FINISH_GPS_FOREGROUND_SERVICE
        val pendingFinishIntent = PendingIntent.getService(this, 0, finishIntent, 0)
        val finishAction =
            NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingFinishIntent)
        notificationBuilder.addAction(finishAction)
    }

    private fun stopForegroundService() {
        gpsLocationProvider.stopOngoingLocationUpdates()
        stopForeground(true)
        stopSelf()
    }

    private fun play() {
        changeNotificationButtonFromPlayToPause()
        NotificationManagerCompat.from(this).notify(CHANNEL_ID_INT, notificationBuilder.build())
        gpsLocationProvider.startOngoingLocationUpdates()
    }

    private fun pause() {
        changeNotificationFromPauseToPlay()
        gpsLocationProvider.stopOngoingLocationUpdates()
    }

    @SuppressLint("RestrictedApi")
    private fun changeNotificationButtonFromPlayToPause() {
        val pauseIntent = Intent(this, GPSForegroundService::class.java)
        pauseIntent.action =
            ACTION_PAUSE_GPS_FOREGROUND_SERVICE
        val pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val pauseAction =
            NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPauseIntent)
        notificationBuilder.mActions[pauseStartActionIndex] = pauseAction
    }

    @SuppressLint("RestrictedApi")
    private fun changeNotificationFromPauseToPlay() {
        val playIntent = Intent(this, GPSForegroundService::class.java)
        playIntent.action = ACTION_PLAY_GPS_FOREGROUND_SERVICE
        val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        val playAction =
            NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent)
        notificationBuilder.mActions[pauseStartActionIndex] = playAction
        NotificationManagerCompat.from(this).notify(CHANNEL_ID_INT, notificationBuilder.build())
    }

}
