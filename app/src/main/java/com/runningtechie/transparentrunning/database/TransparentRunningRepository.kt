package com.runningtechie.transparentrunning.database

import android.content.Context
import androidx.room.Room
import com.runningtechie.transparentrunning.database.dao.LocationPointDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionWithLocationPointsDao
import com.runningtechie.transparentrunning.model.LocationPoint
import com.runningtechie.transparentrunning.model.WorkoutSession

private const val DATABASE_NAME = "transparent-running-database"

class TransparentRunningRepository private constructor() {
    companion object {
        private lateinit var workoutSessionDao: WorkoutSessionDao
        private lateinit var locationPointDao: LocationPointDao
        private lateinit var workoutSessionWithLocationPointsDao: WorkoutSessionWithLocationPointsDao

        fun initialize(context: Context) {
            val database: TransparentRunningDatabase = Room.databaseBuilder(
                context.applicationContext,
                TransparentRunningDatabase::class.java,
                DATABASE_NAME
            ).build()

            workoutSessionDao = database.workoutSessionDao()
            locationPointDao = database.locationPointDao()
            workoutSessionWithLocationPointsDao = database.workoutSessionWithLocationPointsDao()
        }

        fun insertWorkoutSession(workoutSession: WorkoutSession): Long = workoutSessionDao.insert(workoutSession)
        fun insertLocationPoint(locationPoint: LocationPoint): Long = locationPointDao.insert(locationPoint)
        fun getWorkoutSession(): List<WorkoutSession> = workoutSessionDao.getAllWorkoutSessions()
    }
}
