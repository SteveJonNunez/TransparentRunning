package com.runningtechie.transparentrunning.ui.viewLocationPoint

import androidx.lifecycle.ViewModel
import com.runningtechie.transparentrunning.database.TransparentRunningRepository

class LocationPointListViewModel(private val workoutSessionId: Long) : ViewModel() {
    val locationPointListLiveData = TransparentRunningRepository.getLocationPoints(workoutSessionId)
}