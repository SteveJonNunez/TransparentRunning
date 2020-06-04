package com.runningtechie.transparentrunning.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "LocationPoint",
    foreignKeys = [ForeignKey(
        entity = WorkoutSession::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("sessionId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["sessionId"])]
)
data class LocationPoint(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val sessionId: Long,
    val time: Date,
    val elapsedTime: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Distance,
    val speed: Speed,
    val elapsedDistance: Distance
)
