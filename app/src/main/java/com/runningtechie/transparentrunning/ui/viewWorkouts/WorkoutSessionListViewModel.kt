package com.runningtechie.transparentrunning.ui.viewWorkouts

import androidx.lifecycle.ViewModel
import com.runningtechie.transparentrunning.database.TransparentRunningRepository

class WorkoutSessionListViewModel : ViewModel() {
    val workoutListLiveData = TransparentRunningRepository.getWorkoutSessions()
}