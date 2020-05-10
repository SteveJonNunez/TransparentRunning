package com.runningtechie.transparentrunning.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "LocationPoint",
    foreignKeys = [ForeignKey(
        entity = WorkoutSession::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("sessionId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationPoint(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val sessionId: Long,
    val time: Long,
    val elapsedTime: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Float,
    val elapsedDistance: Float
)
