package com.runningtechie.transparentrunning.model

import java.text.NumberFormat

class Distance(val meters: Float) {
    private val numberFormat: NumberFormat by lazy {
        var numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 2
        numberFormat.minimumFractionDigits = 0
        numberFormat
    }
    val miles by lazy { meters * 0.000621371f }
    val feet by lazy { meters / 0.3048f }
    val kilometers by lazy { meters * .001f }
    val yards by lazy { meters * 1.093613298f }

    val metersString: String by lazy { numberFormat.format(meters) }
    val milesString: String by lazy { numberFormat.format(miles) }
    val feetString: String by lazy { numberFormat.format(feet) }
    val kilometersString: String by lazy { numberFormat.format(kilometers) }
    val yardsString: String by lazy { numberFormat.format(yards) }
}