package com.runningtechie.transparentrunning

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*


const val CHANNEL_ID_STRING = "GPS_FOREGROUND_SERVICE"
const val CHANNEL_ID_INT = 1
const val ACTION_START_GPS_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
const val ACTION_STOP_GPS_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
const val ACTION_PAUSE_GPS_FOREGROUND_SERVICE = "ACTION_PAUSE"
const val ACTION_PLAY_GPS_FOREGROUND_SERVICE = "ACTION_PLAY"
const val ACTION_FINISH_GPS_FOREGROUND_SERVICE = "ACTION_FINISH"


class GPSForegroundService : Service() {

    private lateinit var serviceLooper: Looper
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var ongoingLocationRequest: LocationRequest
    private lateinit var singleLocationRequest: LocationRequest
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private val pauseStartActionIndex = 0
    private val finishActionIndex = 1
    private val tag = "GPSForegroundService"

    override fun onCreate() {
        Log.d(tag, "onCreate")

        initializeFusedLocationClient()
        initializeLocationCallback()
        initializeServiceLooper()
        initializeOngoingLocationRequest()
        initializeSinglegLocationRequest()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")

        when (intent.action) {
            ACTION_START_GPS_FOREGROUND_SERVICE -> startForegroundService()
            ACTION_STOP_GPS_FOREGROUND_SERVICE -> stopForegroundService()
            ACTION_PLAY_GPS_FOREGROUND_SERVICE -> play()
            ACTION_PAUSE_GPS_FOREGROUND_SERVICE -> pause()
            ACTION_FINISH_GPS_FOREGROUND_SERVICE -> stopForegroundService()
            else -> throw UnsupportedOperationException("Unsupported intent action: $intent.action")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(tag, "onBind")

        //TODO
        throw UnsupportedOperationException("Not yet implemented.")
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")

    }

    private fun initializeFusedLocationClient() {
        Log.d(tag, "initializeFusedLocationClient")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initializeLocationCallback() {
        Log.d(tag, "initializeLocationCallback")

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    location.hasAltitude()
                    location.altitude
                    location.latitude
                    location.longitude
                    location.hasSpeed()
                    location.speed
                    location.time
                }
            }
        }
    }

    private fun initializeServiceLooper() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            serviceLooper = looper
        }
    }

    private fun initializeOngoingLocationRequest() {
        ongoingLocationRequest = LocationRequest.create()
        ongoingLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        ongoingLocationRequest.interval = 10 * 1000
    }

    private fun initializeSinglegLocationRequest() {
        ongoingLocationRequest = LocationRequest.create()
        ongoingLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        ongoingLocationRequest.numUpdates = 1
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
    private fun startForegroundService() {
        setupOngoingNotification()

        startOngoingLocationUpdates()

        // Start foreground service.
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

        notificationBuilder.setSmallIcon(R.mipmap.sym_def_app_icon)
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        // Create notification default intent.
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        notificationBuilder.setFullScreenIntent(pendingIntent, true)

        // Add Pause button intent in notification.
        val pauseIntent = Intent(this, GPSForegroundService::class.java)
        pauseIntent.action = ACTION_PAUSE_GPS_FOREGROUND_SERVICE
        val pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val pauseAction =
            NotificationCompat.Action(R.drawable.ic_media_play, "Pause", pendingPauseIntent)
        notificationBuilder.addAction(pauseAction)

        // Add Finish button intent in notification.
        val finishIntent = Intent(this, GPSForegroundService::class.java)
        finishIntent.action = ACTION_FINISH_GPS_FOREGROUND_SERVICE
        val pendingFinishIntent = PendingIntent.getService(this, 0, finishIntent, 0)
        val finishAction =
            NotificationCompat.Action(R.drawable.ic_media_pause, "Pause", pendingFinishIntent)
        notificationBuilder.addAction(finishAction)
    }

    private fun stopForegroundService() {
        stopOngoingLocationUpdates()
        stopForeground(true)
        stopSelf()
    }

    private fun play() {
        changeNotificationButtonFromPlayToPause()

        NotificationManagerCompat.from(this).notify(CHANNEL_ID_INT, notificationBuilder.build())
        startOngoingLocationUpdates()
    }

    private fun pause() {
        changeNotificationFromPauseToPlay()

        stopOngoingLocationUpdates()
    }

    @SuppressLint("RestrictedApi")
    private fun changeNotificationButtonFromPlayToPause() {
        val pauseIntent = Intent(this, GPSForegroundService::class.java)
        pauseIntent.action = ACTION_PAUSE_GPS_FOREGROUND_SERVICE
        val pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val pauseAction =
            NotificationCompat.Action(R.drawable.ic_media_pause, "Pause", pendingPauseIntent)
        notificationBuilder.mActions[pauseStartActionIndex] = pauseAction
    }

    @SuppressLint("RestrictedApi")
    private fun changeNotificationFromPauseToPlay() {
        val playIntent = Intent(this, GPSForegroundService::class.java)
        playIntent.action = ACTION_PLAY_GPS_FOREGROUND_SERVICE
        val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        val playAction =
            NotificationCompat.Action(R.drawable.ic_media_play, "Play", pendingPlayIntent)
        notificationBuilder.mActions[pauseStartActionIndex] = playAction
        NotificationManagerCompat.from(this).notify(CHANNEL_ID_INT, notificationBuilder.build())
    }

    private fun startOngoingLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            ongoingLocationRequest,
            locationCallback,
            serviceLooper
        )
    }

    private fun stopOngoingLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.requestLocationUpdates(singleLocationRequest, locationCallback, serviceLooper)
    }

}