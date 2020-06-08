package com.runningtechie.transparentrunning.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
            )
                .addMigrations(MIGRATION_2_3)
                .build()

            workoutSessionDao = database.workoutSessionDao()
            locationPointDao = database.locationPointDao()
            workoutSessionWithLocationPointsDao = database.workoutSessionWithLocationPointsDao()
        }


        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX index_WorkoutSession_id ON WorkoutSession(id);")
                database.execSQL("CREATE INDEX index_LocationPoint_id ON LocationPoint(id);")
            }
        }

        fun insertWorkoutSession(workoutSession: WorkoutSession): Long = workoutSessionDao.insert(workoutSession)
        fun insertLocationPoint(locationPoint: LocationPoint): Long = locationPointDao.insert(locationPoint)
        fun getWorkoutSessions(): LiveData<List<WorkoutSession>> = workoutSessionDao.getAllWorkoutSessions()
        fun getLocationPoints(workoutSessionId: Long): LiveData<List<LocationPoint>> =
            locationPointDao.getAllLocationPoints(workoutSessionId)

        fun updateDurationAndTime(workoutSessionId: Long) = workoutSessionDao.updateDurationAndTime(workoutSessionId)
    }
}
