package com.runningtechy.core.utils

import java.text.NumberFormat

private class NumberFormatter private constructor() {
    companion object {
        private val numberFormat: NumberFormat by lazy {
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 0
            numberFormat
        }

        fun format(float: Float): String =
            numberFormat.format(float)
    }
}

fun Float.toFormattedString(): String {
    return NumberFormatter.format(this)
}
