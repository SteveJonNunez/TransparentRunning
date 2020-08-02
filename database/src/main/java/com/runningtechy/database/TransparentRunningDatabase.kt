package com.runningtechy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.runningtechy.database.dao.LocationPointDao
import com.runningtechy.database.dao.WorkoutSessionDao
import com.runningtechy.database.model.LocationPoint
import com.runningtechy.database.model.WorkoutSession

@Database(entities = [WorkoutSession::class, LocationPoint::class], version = 6)
@TypeConverters(TransparentRunningConverters::class)
abstract class TransparentRunningDatabase : RoomDatabase() {

    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun locationPointDao(): LocationPointDao
}
