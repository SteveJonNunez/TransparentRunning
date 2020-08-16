package com.runningtechy.core.model

import com.runningtechy.core.util.toFormattedString

class Distance(val meters: Float) {
    companion object {
        const val meterInAMile = 0.000621371f
        const val meterInAFeet = 0.3048f
        const val meterInAKilometer = 0.001f
        const val meterInAYard = 1.093613298f
    }
    val miles by lazy { meters * meterInAMile }
    val feet by lazy { meters / meterInAFeet }
    val kilometers by lazy { meters * meterInAKilometer }
    val yards by lazy { meters * meterInAYard }

    val metersString: String by lazy { meters.toFormattedString() }
    val milesString: String by lazy { miles.toFormattedString() }
    val feetString: String by lazy { feet.toFormattedString() }
    val kilometersString: String by lazy { kilometers.toFormattedString() }
    val yardsString: String by lazy { yards.toFormattedString() }
}
