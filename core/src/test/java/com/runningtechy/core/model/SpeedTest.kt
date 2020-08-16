package com.runningtechy.core.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SpeedTest {
    private val floatDelta = 0.001f

    private val metersPerSecond = 132f
    private val metersPerSecondString = "132"

    private val milesPerHour = 295.276f
    private val milesPerHourString = "295.28"

    private val kilometerPerHour = 475.2f
    private val kilometerPerHourString = "475.2"

    private val minutesPerMile = 0.2032f
    private val minutesPerMileString = "12s"

    private val minutesPerKilometer = 0.126262626f
    private val minutesPerKilometerString = "7s"

    private val speed1 = Speed.ofMetersPerSecond(metersPerSecond)
    private val speed2 = Speed.ofDurationInSecondsAndDistanceInMeters(1, 132f)

    @Test
    fun `ofMetersPerSecond`() {
        Assertions.assertEquals(metersPerSecond, speed1.metersPerSecond, floatDelta)
    }

    @Test
    fun `ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(metersPerSecond, speed2.metersPerSecond, floatDelta)
    }

    @Test
    fun `meters per second string format - ofMetersPerSecond`() {
        Assertions.assertEquals(metersPerSecondString, speed1.metersPerSecondString)
    }

    @Test
    fun `meters per second string format - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(metersPerSecondString, speed2.metersPerSecondString)
    }

    @Test
    fun `miles per hour conversion - ofMetersPerSecond`() {
        Assertions.assertEquals(milesPerHour, speed1.milesPerHour, floatDelta)
    }

    @Test
    fun `miles per hour conversion - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(milesPerHour, speed2.milesPerHour, floatDelta)
    }

    @Test
    fun `miles per hour string format - ofMetersPerSecond`() {
        Assertions.assertEquals(milesPerHourString, speed1.milesPerHourString)
    }

    @Test
    fun `miles per hour string format - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(milesPerHourString, speed2.milesPerHourString)
    }

    @Test
    fun `kilometer per hour conversion - ofMetersPerSecond`() {
        Assertions.assertEquals(kilometerPerHour, speed1.kilometerPerHour, floatDelta)
    }

    @Test
    fun `kilometer per hour conversion - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(kilometerPerHour, speed2.kilometerPerHour, floatDelta)
    }

    @Test
    fun `kilometer per hour string format - ofMetersPerSecond`() {
        Assertions.assertEquals(kilometerPerHourString, speed1.kilometersPerHourString)
    }

    @Test
    fun `kilometer per hour string format - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(kilometerPerHourString, speed2.kilometersPerHourString)
    }

    @Test
    fun `minutes per mile conversion - ofMetersPerSecond`() {
        Assertions.assertEquals(minutesPerMile, speed1.minutesPerMile, floatDelta)
    }

    @Test
    fun `minutes per mile conversion - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(minutesPerMile, speed2.minutesPerMile, floatDelta)
    }

    @Test
    fun `minutes per mile string format - ofMetersPerSecond`() {
        Assertions.assertEquals(minutesPerMileString, speed1.minutesPerMileString)
    }

    @Test
    fun `minutes per mile string format - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(minutesPerMileString, speed2.minutesPerMileString)
    }

    @Test
    fun `minutes per kilometer conversion - ofMetersPerSecond`() {
        Assertions.assertEquals(minutesPerKilometer, speed1.minutesPerKilometer, floatDelta)
    }

    @Test
    fun `minutes per kilometer conversion - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(minutesPerKilometer, speed2.minutesPerKilometer, floatDelta)
    }

    @Test
    fun `minutes per kilometers string format - ofMetersPerSecond`() {
        Assertions.assertEquals(minutesPerKilometerString, speed1.minutesPerKilometerString)
    }

    @Test
    fun `minutes per kilometers string format - ofDurationInSecondsAndDistanceInMeters`() {
        Assertions.assertEquals(minutesPerKilometerString, speed2.minutesPerKilometerString)
    }
}
