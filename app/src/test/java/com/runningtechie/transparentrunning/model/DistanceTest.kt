package com.runningtechie.transparentrunning.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class DistanceTest {
    private val meters = 12f
    private val kilometers = 0.012f
    private val yards = 13.12335958f
    private val feet = 39.37007874f
    private val miles = .0074564517f

    private val distance = Distance(meters)


    @Test
    fun `constructor param is accessible`() {
        assertEquals(meters, distance.meters)
    }

    @Test
    fun `feet conversion`() {
        assertEquals(feet, distance.feet)
    }

    @Test
    fun `miles conversion`() {
        assertEquals(miles, distance.miles)
    }

    @Test
    fun `kilometers conversion`() {
        assertEquals(kilometers, distance.kilometers)
    }

    @Test
    fun `yards conversion`() {
        assertEquals(yards, distance.yards)
    }
}