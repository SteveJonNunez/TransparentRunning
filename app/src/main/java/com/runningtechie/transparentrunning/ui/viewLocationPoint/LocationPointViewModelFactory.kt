package com.runningtechie.transparentrunning.ui.viewLocationPoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationPointViewModelFactory(private val workoutSessionId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Long::class.java).newInstance(workoutSessionId)
    }
}