package com.runningtechie.transparentrunning.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.runningtechie.transparentrunning.model.WorkoutSession
import com.runningtechie.transparentrunning.model.WorkoutSessionWithLocationPoints

@Dao
interface WorkoutSessionWithLocationPointsDao {

    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE id=(:workoutSessionId)")
    fun getWorkoutSession(workoutSessionId: Int): WorkoutSessionWithLocationPoints
}
