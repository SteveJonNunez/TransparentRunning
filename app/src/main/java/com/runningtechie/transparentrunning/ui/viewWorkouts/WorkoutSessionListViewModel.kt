package com.runningtechie.transparentrunning.ui.viewWorkouts

import androidx.lifecycle.ViewModel
import com.runningtechy.database.TransparentRunningRepository

class WorkoutSessionListViewModel : ViewModel() {
    val workoutListLiveData = TransparentRunningRepository.getWorkoutSessions()
}
