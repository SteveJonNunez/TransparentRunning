package com.runningtechie.transparentrunning

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.runningtechie.transparentrunning.database.TransparentRunningRepository
import com.runningtechie.transparentrunning.model.Distance
import com.runningtechie.transparentrunning.model.LocationPoint
import com.runningtechie.transparentrunning.model.Speed
import java.util.*

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

    private var backgroundHandler: BackgroundHandler = BackgroundHandler("GPSLocationProvider")

    private var startTime: Long = 0L
    private var elapsedDistance: Float = 0.0F
    private var previousLocation: Location? = null

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

    private fun insertLocationPoint(location: Location) {
        if (previousLocation != null)
            elapsedDistance += location.distanceTo(previousLocation)
        else
            startTime = location.time

        TransparentRunningRepository.insertLocationPoint(
            LocationPoint(
                sessionId = workoutSessionId,
                time = Date(location.time),
                elapsedTime = (location.time - startTime),
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = Distance(location.altitude.toFloat()),
                speed = Speed(location.speed),
                elapsedDistance = Distance(elapsedDistance)
            )
        )
        previousLocation = location
    }

    private fun updateWorkoutSessionDurationAndTime() {
        TransparentRunningRepository.updateDurationAndTime(workoutSessionId)
    }

    private fun createOngoingLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL_TIME
        return locationRequest
    }

    private fun createSingleLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        return locationRequest
    }

    companion object {
        private const val INTERVAL_TIME: Long = 10 * 1000
    }

}
