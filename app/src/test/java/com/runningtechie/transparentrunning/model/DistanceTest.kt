package com.runningtechie.transparentrunning.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class DistanceTest {
    private val meters = 12f
    private val metersString = "12"
    private val kilometers = 0.012f
    private val kilometersString = "0.01"
    private val yards = 13.12335958f
    private val yardsString = "13.12"
    private val feet = 39.37007874f
    private val feetString = "39.37"
    private val miles = .0074564517f
    private val milesString = "0.01"

    private val distance = com.runningtechy.core.model.Distance(meters)


    @Test
    fun `constructor param is accessible`() {
        assertEquals(meters, distance.meters)
    }

    @Test
    fun `meters string format`() {
        assertEquals(metersString, distance.metersString)
    }

    @Test
    fun `feet conversion`() {
        assertEquals(feet, distance.feet)
    }

    @Test
    fun `feet string`() {
        assertEquals(feetString, distance.feetString)
    }

    @Test
    fun `miles conversion`() {
        assertEquals(miles, distance.miles)
    }

    @Test
    fun `miles string`() {
        assertEquals(milesString, distance.milesString)
    }

    @Test
    fun `kilometers conversion`() {
        assertEquals(kilometers, distance.kilometers)
    }

    @Test
    fun `kilometers string`() {
        assertEquals(kilometersString, distance.kilometersString)
    }

    @Test
    fun `yards conversion`() {
        assertEquals(yards, distance.yards)
    }

    @Test
    fun `yards string`() {
        assertEquals(yardsString, distance.yardsString)
    }
}
