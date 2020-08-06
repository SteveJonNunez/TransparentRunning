package com.runningtechy.core.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ActivityHelperTest {

    @Test
    fun `GPSForegroundService AddressableActivity class name`() {
        Assertions.assertEquals("com.runningtechy.gpsForegroundService.GPSForegroundService", Activities.GPSForegroundService.className)
    }
}