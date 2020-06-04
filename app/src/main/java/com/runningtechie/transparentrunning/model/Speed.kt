package com.runningtechie.transparentrunning.model

import com.runningtechie.transparentrunning.toFormattedString

class Speed(val metersPerSecond: Float) {
    val milesPerHour by lazy { metersPerSecond * 60 * 60 * Distance.meterToMile }
    val kilometerPerHour by lazy { metersPerSecond * 60f * 60f * Distance.meterToKilometer }
    val minutesPerMile by lazy { metersPerSecond * (1 / (60 * Distance.meterToMile)) }
    val minutesPerKilometer by lazy { metersPerSecond * (1 / (60 * Distance.meterToKilometer)) }

    val metersPerSecondString: String by lazy { metersPerSecond.toFormattedString() }
    val milesPerHourString: String by lazy { milesPerHour.toFormattedString() }
    val kilometersPerHourString: String by lazy { kilometerPerHour.toFormattedString() }
    val minutesPerMileString: String by lazy { minutesPerMile.toFormattedString() }
    val minutesPerKilometerString: String by lazy { minutesPerKilometer.toFormattedString() }
}