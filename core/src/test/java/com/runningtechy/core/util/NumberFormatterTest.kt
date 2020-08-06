package com.runningtechy.core.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NumberFormatterTest {

    @Test
    fun `more than two decimal places, no rounding`() {
        val float = 12.34343f
        assertEquals("12.34", float.toFormattedString())
    }

    @Test
    fun `more than two decimal places, rounding`() {
        val float = 12.34943f
        assertEquals("12.35", float.toFormattedString())
    }

    @Test
    fun `two decimal places`() {
        val float = 12.38f
        assertEquals("12.38", float.toFormattedString())
    }

    @Test
    fun `one decimal place`() {
        val float = 12.3f
        assertEquals("12.3", float.toFormattedString())
    }

    @Test
    fun `no decimal place`() {
        val float = 12f
        assertEquals("12", float.toFormattedString())
    }

    @Test
    fun `zero decimal place`() {
        val float = 12.0f
        assertEquals("12", float.toFormattedString())
    }
}