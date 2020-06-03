package com.runningtechie.transparentrunning.database

import androidx.room.TypeConverter
import java.util.*

class TransparentRunningConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisecondSinceEpoch: Long?): Date? {
        return millisecondSinceEpoch?.let { Date(it) }
    }

    @TypeConverter
    fun toFloat(doubleValue: Double?): Float? {
        return doubleValue?.toFloat()
    }

    @TypeConverter
    fun toDouble(floatValue: Float?): Double? {
        return floatValue?.toDouble()
    }
}
