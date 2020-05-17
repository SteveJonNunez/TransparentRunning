package com.runningtechie.transparentrunning.ui.viewWorkouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkoutSessionViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}
