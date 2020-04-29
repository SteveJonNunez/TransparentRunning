package com.runningtechie.transparentrunning

import android.app.Application
import com.runningtechie.transparentrunning.database.TransparentRunningRepository

class TransparentRunningApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        TransparentRunningRepository.initialize(this)
    }
}