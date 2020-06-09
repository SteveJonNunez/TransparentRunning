package com.runningtechie.transparentrunning.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SpeedTest {
    private val floatDelta = 0.001f

    private val metersPerSecond = 1f
    private val metersPerSecondString = "1"

    private val milesPerHour = 2.2369356f
    private val milesPerHourString = "2.24"

    private val kilometerPerHour = 3.6f
    private val kilometerPerHourString = "3.6"

    private val minutesPerMile = 26.822409f
    private val minutesPerMileString = "26.82"

    private val minutesPerKilometer = 16.666666f
    private val minutesPerKilometerString = "16.67"

    private val speed = Speed(metersPerSecond)

    @Test
    fun `constructor param is accessible`() {
        Assertions.assertEquals(metersPerSecond, speed.metersPerSecond, floatDelta)
    }

    @Test
    fun `meters per second string format`() {
        Assertions.assertEquals(metersPerSecondString, speed.metersPerSecondString)
    }

    @Test
    fun `miles per hour conversion`() {
        Assertions.assertEquals(milesPerHour, speed.milesPerHour, floatDelta)
    }

    @Test
    fun `miles per hour string format`() {
        Assertions.assertEquals(milesPerHourString, speed.milesPerHourString)
    }

    @Test
    fun `kilometer per hour conversion`() {
        Assertions.assertEquals(kilometerPerHour, speed.kilometerPerHour, floatDelta)
    }

    @Test
    fun `kilometer per hour string format`() {
        Assertions.assertEquals(kilometerPerHourString, speed.kilometersPerHourString)
    }

    @Test
    fun `minutes per mile conversion`() {
        Assertions.assertEquals(minutesPerMile, speed.minutesPerMile, floatDelta)
    }

    @Test
    fun `minutes per mile string format`() {
        Assertions.assertEquals(minutesPerMileString, speed.minutesPerMileString)
    }

    @Test
    fun `minutes per kilometer conversion`() {
        Assertions.assertEquals(minutesPerKilometer, speed.minutesPerKilometer, floatDelta)
    }

    @Test
    fun `minutes per kilometers string format`() {
        Assertions.assertEquals(minutesPerKilometerString, speed.minutesPerKilometerString)
    }
}
