package com.runningtechy.core.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DurationTest {

    @Test
    fun `test hours`() {
        val hours = 3
        val minutes = 45
        val seconds = 12
        val duration =
            Duration.ofMilliseconds(1000L * 60 * 60 * hours + 1000 * 60 * minutes + 1000 * seconds)

        assertEquals("3h:45m:12s", duration.toString())
    }
}
