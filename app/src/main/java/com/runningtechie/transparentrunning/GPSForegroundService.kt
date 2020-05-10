package com.runningtechie.transparentrunning

import android.R
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Process
import android.os.Looper
import android.os.HandlerThread
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.runningtechie.transparentrunning.database.TransparentRunningRepository
import com.runningtechie.transparentrunning.model.LocationPoint


const val CHANNEL_ID_STRING = "GPS_FOREGROUND_SERVICE"
const val CHANNEL_ID_INT = 1
const val ACTION_START_GPS_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
const val ACTION_STOP_GPS_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
const val ACTION_PAUSE_GPS_FOREGROUND_SERVICE = "ACTION_PAUSE"
const val ACTION_PLAY_GPS_FOREGROUND_SERVICE = "ACTION_PLAY"
const val ACTION_FINISH_GPS_FOREGROUND_SERVICE = "ACTION_FINISH"

const val WORKOUT_SESSION_ID_KEY = "WORKOUT_SESSION_ID_KEY"


class GPSForegroundService : Service() {
    companion object {
        const val INTERVAL_TIME: Long = 10 * 1000

        fun stopGpsForegroundService(context: Context) {
            val intent = Intent(context, GPSForegroundService::class.java)
            intent.action = ACTION_STOP_GPS_FOREGROUND_SERVICE
            ContextCompat.startForegroundService(context, intent)
        }

        fun startGpsForegroundService(context: Context, workoutSessionId: Long) {
            var intent = Intent(context, GPSForegroundService::class.java)
            intent.action = ACTION_START_GPS_FOREGROUND_SERVICE
            intent.putExtra(WORKOUT_SESSION_ID_KEY, workoutSessionId)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    private lateinit var handlerThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private lateinit var uiHandler: Handler
    private lateinit var transparentRunningRepository: TransparentRunningRepository
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var ongoingLocationRequest: LocationRequest
    private lateinit var singleLocationRequest: LocationRequest
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private var startTime: Long = 0L
    private var elapsedDistance: Float = 0.0F
    private var previousLocation: Location? = null

    private val pauseStartActionIndex = 0
    private val finishActionIndex = 1
    private val tag = "GPSForegroundService"

    override fun onCreate() {
        Log.d(tag, "onCreate")

        initializeFusedLocationClient()
        initializeLocationCallback()
        initializeOngoingLocationRequest()
        initializeSinglegLocationRequest()
        initializeHandlerThread()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")

        when (intent.action) {
            ACTION_START_GPS_FOREGROUND_SERVICE -> startForegroundService(intent.getLongExtra(WORKOUT_SESSION_ID_KEY, 0L))
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
        throw UnsupportedOperationException("Not yet implemented.")
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")

    }

    private fun initializeHandler() {
        backgroundHandler = Handler(getBackgroundLooper())
    }

    private fun initializeHandlerThread() {
        Log.d(tag, "initializeHandlerThread")
        handlerThread = HandlerThread("GpsBackgroundThread", Process.THREAD_PRIORITY_BACKGROUND)
        initializeHandler()
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
                Log.d(tag, "onLocationResult")

                for (location in locationResult.locations) {
                    if (previousLocation != null) {
                        elapsedDistance += location.distanceTo(previousLocation)
                        startTime = location.time
                    }
                    TransparentRunningRepository.insertLocationPoint(
                        LocationPoint(
                            sessionId = 1L,
                            time = location.time,
                            elapsedTime = location.time - startTime,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            altitude = location.altitude,
                            speed = location.speed,
                            elapsedDistance = elapsedDistance
                        )
                    )

                    previousLocation = location
                }
            }
        }
    }

    private fun initializeOngoingLocationRequest() {
        ongoingLocationRequest = LocationRequest.create()
        ongoingLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        ongoingLocationRequest.interval = INTERVAL_TIME
    }

    private fun initializeSinglegLocationRequest() {
        singleLocationRequest = LocationRequest.create()
        singleLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        singleLocationRequest.numUpdates = 1
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
        setupOngoingNotification()
        startOngoingLocationUpdates()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely()
        } else {
            handlerThread.quit()
        }
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
            getBackgroundLooper()
        )
    }

    private fun stopOngoingLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.requestLocationUpdates(
            singleLocationRequest,
            locationCallback,
            getBackgroundLooper()
        )
    }

    private fun getBackgroundLooper(): Looper {
        if (!handlerThread.isAlive)
            handlerThread.start()

        return handlerThread.looper
    }

}
