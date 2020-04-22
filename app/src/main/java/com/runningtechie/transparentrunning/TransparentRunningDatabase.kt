package com.runningtechie.transparentrunning

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.runningtechie.transparentrunning.enitity.LocationPoint
import com.runningtechie.transparentrunning.enitity.WorkoutSession

@Database(entities = [WorkoutSession::class, LocationPoint::class], version = 1)
@TypeConverters(TransparentRunningConverters::class)
abstract class TransparentRunningDatabase : RoomDatabase()