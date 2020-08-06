package com.runningtechy.core.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NumberFormatterTest {

    @Test
    fun `more than two decimal places, no rounding`() {
        val float = 12.34343f
        assertEquals(float.toFormattedString(), "12.34")
    }

    @Test
    fun `more than two decimal places, rounding`() {
        val float = 12.34943f
        assertEquals(float.toFormattedString(), "12.35")
    }

    @Test
    fun `two decimal places`() {
        val float = 12.38f
        assertEquals(float.toFormattedString(), "12.38")
    }

    @Test
    fun `one decimal place`() {
        val float = 12.3f
        assertEquals(float.toFormattedString(), "12.3")
    }

    @Test
    fun `no decimal place`() {
        val float = 12f
        assertEquals(float.toFormattedString(), "12")
    }

    @Test
    fun `zero decimal place`() {
        val float = 12.0f
        assertEquals(float.toFormattedString(), "12")
    }
}