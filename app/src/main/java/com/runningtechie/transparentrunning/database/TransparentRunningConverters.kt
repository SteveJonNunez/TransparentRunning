package com.runningtechie.transparentrunning.database

import androidx.room.TypeConverter
import com.runningtechie.transparentrunning.model.Distance
import java.util.*

class TransparentRunningConverters {
    companion object {

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): Long? {
            return date?.time
        }

        @TypeConverter
        @JvmStatic
        fun toDate(millisecondSinceEpoch: Long?): Date? {
            return millisecondSinceEpoch?.let { Date(it) }
        }

        @TypeConverter
        @JvmStatic
        fun fromDistance(distance: Distance?): Float? {
            return distance?.meters
        }

        @TypeConverter
        @JvmStatic
        fun toDistance(distanceInMeters: Float?): Distance? {
            return distanceInMeters?.let { Distance(it) }
        }
    }
}
