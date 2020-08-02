package com.runningtechy.core.model

class Duration private constructor(val milliseconds: Long) {
    companion object {
        private const val MILLISECONDS_IN_A_SECOND = 1000
        private const val SECONDS_IN_A_MINUTE = 60
        private const val MINUTES_IN_AN_HOUR = 60
        private const val MILLISECONDS_IN_AN_HOUR = MILLISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * MINUTES_IN_AN_HOUR
        private const val MILLISECONDS_IN_A_MINUTE = MILLISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE


        fun ofMilliseconds(milliseconds: Long) = Duration(milliseconds)
    }

    override fun toString(): String {
        var hoursString = ""
        var minutesString = ""
        var secondsString = ""

        val hoursValue = milliseconds / (MILLISECONDS_IN_AN_HOUR)
        if (hoursValue > 0)
            hoursString = "${hoursValue}h:"


        val minutesValue =
            milliseconds % (MILLISECONDS_IN_AN_HOUR) / (MILLISECONDS_IN_A_MINUTE)
        if (hoursString.isNotEmpty() || minutesValue > 0)
            minutesString = "${minutesValue}m:"


        secondsString = "${milliseconds % (MILLISECONDS_IN_A_MINUTE) / MILLISECONDS_IN_A_SECOND}s"

        return "${hoursString}${minutesString}${secondsString}"
    }


}
