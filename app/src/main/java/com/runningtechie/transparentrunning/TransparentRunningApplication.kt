package com.runningtechie.transparentrunning

import android.app.Application
import com.runningtechie.transparentrunning.database.TransparentRunningRepository
import timber.log.Timber
import timber.log.Timber.DebugTree


class TransparentRunningApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        TransparentRunningRepository.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
