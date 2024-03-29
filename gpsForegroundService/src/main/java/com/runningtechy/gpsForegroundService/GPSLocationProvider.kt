package com.runningtechy.gpsForegroundService

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.runningtechie.transparentrunning.BackgroundHandler
import com.runningtechy.core.model.Distance
import com.runningtechy.core.model.Duration
import com.runningtechy.core.model.Speed
import com.runningtechy.database.TransparentRunningRepository
import com.runningtechy.database.model.LocationPoint
import java.util.*
import kotlin.math.round

class GPSLocationProvider(private var workoutSessionId: Long, gpsForegroundService: GPSForegroundService) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(gpsForegroundService)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations)
                insertLocationPoint(location)
        }
    }

    private var finalLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations)
                insertLocationPoint(location)
            updateWorkoutSessionDurationAndTime()
            backgroundHandler.quit()
        }
    }

    private var ongoingLocationRequest: LocationRequest = createOngoingLocationRequest()
    private var singleLocationRequest: LocationRequest = createSingleLocationRequest()

    private var backgroundHandler: BackgroundHandler =
        BackgroundHandler("GPSLocationProvider")

    private var startTime: Long = 0L
    private var elapsedDistance: Float = 0.0F
    private var previousLocationPoint: LocationPoint? = null


    @SuppressLint("MissingPermission")
    fun startOngoingLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            ongoingLocationRequest,
            locationCallback,
            backgroundHandler.getLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun stopOngoingLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.requestLocationUpdates(
            singleLocationRequest,
            finalLocationCallback,
            backgroundHandler.getLooper()
        )
    }

    private fun insertLocationPoint(currentLocation: Location) {
        if (previousLocationPoint != null) {
            val results = FloatArray(3)
            Location.distanceBetween(
                previousLocationPoint!!.latitude, previousLocationPoint!!.longitude,
                currentLocation.latitude, currentLocation.longitude,
                results
            )
            elapsedDistance += results[0]
        } else
            startTime = currentLocation.time

        val elapsedTime = Duration.ofMilliseconds(currentLocation.time - startTime)

        val locationPoint = LocationPoint(
            sessionId = workoutSessionId,
            time = Date(currentLocation.time),
            elapsedTime = elapsedTime,
            roundedElapsedTime = Duration.ofMilliseconds(
                (round(elapsedTime.milliseconds / MILLISECONDS_IN_SECONDS_DOUBLE)
                        * MILLISECONDS_IN_SECONDS_INT).toLong()
            ),
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude,
            altitude = if (currentLocation.hasAltitude()) Distance(currentLocation.altitude.toFloat()) else null,
            speed = if (currentLocation.hasSpeed()) Speed.ofMetersPerSecond(currentLocation.speed) else null,
            bearing = if (currentLocation.hasBearing()) currentLocation.bearing else null,
            elapsedDistance = Distance(elapsedDistance),
            horizontalAccuracy = if (currentLocation.hasAccuracy()) currentLocation.accuracy else null,
            verticalAccuracy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (currentLocation.hasVerticalAccuracy()) currentLocation.verticalAccuracyMeters else null
            } else null,
            speedAccuracy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (currentLocation.hasSpeedAccuracy()) currentLocation.speedAccuracyMetersPerSecond else null
            } else null,
            bearingAccuracy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (currentLocation.hasBearingAccuracy()) currentLocation.bearingAccuracyDegrees else null
            } else null,
            isSimulated = false
        )
        TransparentRunningRepository.insertLocationPoint(locationPoint)

        previousLocationPoint = locationPoint
    }

    private fun updateWorkoutSessionDurationAndTime() {
        TransparentRunningRepository.updateDurationAndTime(workoutSessionId)
    }

    private fun createOngoingLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL_TIME
        locationRequest.fastestInterval = FASTEST_INTERVAL_TIME
        return locationRequest
    }

    private fun createSingleLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        return locationRequest
    }

    companion object {
        const val INTERVAL_TIME: Long = 5 * 1000
        const val FASTEST_INTERVAL_TIME: Long = 1 * 1000
        const val MILLISECONDS_IN_SECONDS_DOUBLE: Double = 1000.0
        const val MILLISECONDS_IN_SECONDS_INT: Int = 1000
    }

}
