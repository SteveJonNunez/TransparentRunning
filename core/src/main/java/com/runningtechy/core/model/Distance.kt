package com.runningtechy.core.model

import com.runningtechy.core.utils.toFormattedString

class Distance(val meters: Float) {
    companion object {
        const val meterToMile = 0.000621371f
        const val meterToFeet = 0.3048f
        const val meterToKilometer = 0.001f
        const val meterToYard = 1.093613298f
    }
    val miles by lazy { meters * meterToMile }
    val feet by lazy { meters / meterToFeet }
    val kilometers by lazy { meters * meterToKilometer }
    val yards by lazy { meters * meterToYard }

    val metersString: String by lazy { meters.toFormattedString() }
    val milesString: String by lazy { miles.toFormattedString() }
    val feetString: String by lazy { feet.toFormattedString() }
    val kilometersString: String by lazy { kilometers.toFormattedString() }
    val yardsString: String by lazy { yards.toFormattedString() }
}
