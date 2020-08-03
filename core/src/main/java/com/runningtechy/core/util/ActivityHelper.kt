package com.runningtechy.core.util

import android.content.Context
import android.content.Intent

private const val PACKAGE_NAME = "com.runningtechy"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
        addressableActivity.className)
}

fun intentTo(addressableActivity: AddressableActivity, action: String, context: Context): Intent {
    return Intent(action).setClassName(
        context,
        addressableActivity.className)
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object Activities {
    /**
     * GPSForegroundService
     */
    object GPSForegroundService : AddressableActivity {
        override val className = "$PACKAGE_NAME.gpsForegroundService.GPSForegroundService"
    }
}