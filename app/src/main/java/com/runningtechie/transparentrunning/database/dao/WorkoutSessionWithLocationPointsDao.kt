package com.runningtechie.transparentrunning.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.runningtechie.transparentrunning.model.WorkoutSession
import com.runningtechie.transparentrunning.model.WorkoutSessionWithLocationPoints

@Dao
interface WorkoutSessionWithLocationPointsDao {

    @Query("SELECT * FROM WorkoutSession WHERE id=(:id)")
    fun getWorkoutSession(id: Int): WorkoutSessionWithLocationPoints
}