package com.runningtechie.transparentrunning.ui.viewLocationPoint

import androidx.lifecycle.ViewModel
import com.runningtechy.database.TransparentRunningRepository

class LocationPointListViewModel(workoutSessionId: Long) : ViewModel() {
    val locationPointListLiveData = TransparentRunningRepository.getLocationPoints(workoutSessionId)
}
