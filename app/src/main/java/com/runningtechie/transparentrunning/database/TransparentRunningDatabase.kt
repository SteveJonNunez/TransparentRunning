package com.runningtechie.transparentrunning.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.runningtechie.transparentrunning.database.dao.LocationPointDao
import com.runningtechie.transparentrunning.database.dao.WorkoutSessionDao
import com.runningtechie.transparentrunning.model.LocationPoint
import com.runningtechie.transparentrunning.model.WorkoutSession

@Database(entities = [WorkoutSession::class, LocationPoint::class], version = 5)
@TypeConverters(TransparentRunningConverters::class)
abstract class TransparentRunningDatabase : RoomDatabase() {

    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun locationPointDao(): LocationPointDao
}
