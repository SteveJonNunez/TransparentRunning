package com.runningtechy.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.runningtechy.database.model.WorkoutSession

@Dao
interface WorkoutSessionDao: BaseDao<WorkoutSession> {

    @Query("SELECT * FROM WorkoutSession ORDER BY date DESC")
    fun getAllWorkoutSessions(): LiveData<List<WorkoutSession>>

    @Query("SELECT * FROM WorkoutSession")
    fun getAllWorkoutSessionsAsync(): List<WorkoutSession>

    @Query("SELECT * FROM WorkoutSession WHERE id=(:id)")
    fun getWorkoutSession(id: Long): WorkoutSession

    @Query("UPDATE WorkoutSession\n" +
            "SET (duration, distance) = \n" +
            "(SELECT MAX(elapsedTime), MAX(elapsedDistance)\n" +
            "FROM LocationPoint lp\n" +
            "WHERE WorkoutSession.id = lp.sessionId)\n" +
            "WHERE id = (:id)")
    fun updateDurationAndTime(id: Long)
}
