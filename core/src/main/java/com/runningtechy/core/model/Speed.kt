package com.runningtechy.core.model

import com.runningtechy.core.util.toFormattedString

class Speed private constructor(val metersPerSecond: Float) {
    companion object {
        private const val secondsInAMinute = 60f
        private const val minutesInAHour = 60f

        fun ofMetersPerSecond(metersPerSecond: Float) = Speed(metersPerSecond)
        fun ofDurationInSecondsAndDistanceInMeters(durationInSeconds: Long, distanceInMeters: Float) = Speed(distanceInMeters/durationInSeconds)
    }

    val milesPerHour by lazy { metersPerSecond * secondsInAMinute * minutesInAHour * Distance.meterInAMile }
    val kilometerPerHour by lazy { metersPerSecond * secondsInAMinute * minutesInAHour * Distance.meterInAKilometer }
    val minutesPerMile by lazy { 1 / (metersPerSecond * secondsInAMinute * Distance.meterInAMile) }
    val minutesPerKilometer by lazy { 1 / (metersPerSecond * secondsInAMinute * Distance.meterInAKilometer) }
    val minutesPerMileDuration by lazy { Duration.ofMinutes(minutesPerMile.toDouble()) }
    val minutesPerKilometerDuration by lazy { Duration.ofMinutes(minutesPerKilometer.toDouble()) }

    val metersPerSecondString: String by lazy { metersPerSecond.toFormattedString() }
    val milesPerHourString: String by lazy { milesPerHour.toFormattedString() }
    val kilometersPerHourString: String by lazy { kilometerPerHour.toFormattedString() }
    val minutesPerMileString: String by lazy { minutesPerMileDuration.toString() }
    val minutesPerKilometerString: String by lazy { minutesPerKilometerDuration.toString() }
}
