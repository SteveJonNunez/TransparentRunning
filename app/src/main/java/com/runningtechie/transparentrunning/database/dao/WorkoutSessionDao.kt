package com.runningtechie.transparentrunning.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.runningtechie.transparentrunning.model.WorkoutSession

@Dao
interface WorkoutSessionDao {

    @Query("SELECT * FROM WorkoutSession")
    fun getAllWorkoutSessions(): List<WorkoutSession>

    @Query("SELECT * FROM WorkoutSession WHERE id=(:id)")
    fun getWorkoutSession(id: Int): WorkoutSession
}