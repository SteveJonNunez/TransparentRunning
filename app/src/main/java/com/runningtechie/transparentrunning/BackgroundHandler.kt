package com.runningtechie.transparentrunning

import android.os.*

class BackgroundHandler(threadName: String) {
    private var handlerThread: HandlerThread = HandlerThread(threadName, Process.THREAD_PRIORITY_BACKGROUND)
    private var handler: Handler

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun getLooper() = handlerThread.looper

    fun quit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely()
        } else {
            handlerThread.quit()
        }
    }
}
