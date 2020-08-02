package com.runningtechy.core.model

import com.runningtechy.core.utils.toFormattedString

class Speed(val metersPerSecond: Float) {
    companion object {
        private const val secondsInAMinute = 60f
        private const val minutesInAHour = 60f
    }

    val milesPerHour by lazy { metersPerSecond * secondsInAMinute * minutesInAHour * Distance.meterToMile }
    val kilometerPerHour by lazy { metersPerSecond * secondsInAMinute * minutesInAHour * Distance.meterToKilometer }
    val minutesPerMile by lazy { metersPerSecond * (1 / (secondsInAMinute * Distance.meterToMile)) }
    val minutesPerKilometer by lazy { metersPerSecond * (1 / (secondsInAMinute * Distance.meterToKilometer)) }

    val metersPerSecondString: String by lazy { metersPerSecond.toFormattedString() }
    val milesPerHourString: String by lazy { milesPerHour.toFormattedString() }
    val kilometersPerHourString: String by lazy { kilometerPerHour.toFormattedString() }
    val minutesPerMileString: String by lazy { minutesPerMile.toFormattedString() }
    val minutesPerKilometerString: String by lazy { minutesPerKilometer.toFormattedString() }
}
