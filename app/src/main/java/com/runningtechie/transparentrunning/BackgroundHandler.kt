package com.runningtechie.transparentrunning

import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process

class BackgroundHandler(threadName: String) {
    private var handlerThread: HandlerThread = HandlerThread(threadName, Process.THREAD_PRIORITY_BACKGROUND)
    private var handler: Handler

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun getLooper(): Looper = handlerThread.looper

    fun quit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely()
        } else {
            handlerThread.quit()
        }
    }
}
