package com.runningtechie.transparentrunning.database

import android.content.Context
import androidx.room.Room
import com.runningtechie.transparentrunning.database.dao.LocationPointDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionWithLocationPointsDao
import com.runningtechie.transparentrunning.model.LocationPoint
import com.runningtechie.transparentrunning.model.WorkoutSession

private const val DATABASE_NAME = "transparent-running-database"

class TransparentRunningRepository private constructor(context: Context) {

    private val database : TransparentRunningDatabase = Room.databaseBuilder(
        context.applicationContext,
        TransparentRunningDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val workoutSessionDao = database.workoutSessionDao()
    private val locationPointDao = database.locationPointDao()
    private val workoutSessionWithLocationPointsDao = database.workoutSessionWithLocationPointsDao()

    fun insertWorkoutSession(workoutSession: WorkoutSession): Long = workoutSessionDao.insert(workoutSession)
    fun insertLocationPoint(locationPoint: LocationPoint): Long =  locationPointDao.insert(locationPoint)
    fun getWorkoutSession(): List<WorkoutSession> = workoutSessionDao.getAllWorkoutSessions()

    companion object {
        private var INSTANCE: TransparentRunningRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TransparentRunningRepository(context)
            }
        }

        fun get(): TransparentRunningRepository {
            return INSTANCE
                ?: throw IllegalStateException("TransparentRunningRepository must be initialized")
        }
    }
}