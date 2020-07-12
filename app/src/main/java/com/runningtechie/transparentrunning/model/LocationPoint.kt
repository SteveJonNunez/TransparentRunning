package com.runningtechie.transparentrunning.model

import androidx.room.ColumnInfo
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Long? = null,

    @ColumnInfo(name= "sessionId", typeAffinity = ColumnInfo.INTEGER, index = true)
    val sessionId: Long,

    @ColumnInfo(name= "time", typeAffinity = ColumnInfo.INTEGER, index = false)
    val time: Date,

    @ColumnInfo(name= "elapsedTime", typeAffinity = ColumnInfo.INTEGER, index = false)
    val elapsedTime: Duration,

    @ColumnInfo(name= "roundedElapsedTime", typeAffinity = ColumnInfo.INTEGER, index = false)
    val roundedElapsedTime: Duration,

    @ColumnInfo(name= "latitude", typeAffinity = ColumnInfo.REAL, index = false)
    val latitude: Double,

    @ColumnInfo(name= "longitude", typeAffinity = ColumnInfo.REAL, index = false)
    val longitude: Double,

    @ColumnInfo(name= "altitude", typeAffinity = ColumnInfo.REAL, index = false)
    val altitude: Distance? = null,

    @ColumnInfo(name= "speed", typeAffinity = ColumnInfo.REAL, index = false)
    val speed: Speed? = null,

    @ColumnInfo(name= "bearing", typeAffinity = ColumnInfo.REAL, index = false)
    val bearing: Float? = null,

    @ColumnInfo(name= "elapsedDistance", typeAffinity = ColumnInfo.REAL, index = false)
    val elapsedDistance: Distance,

    @ColumnInfo(name= "horizontalAccuracy", typeAffinity = ColumnInfo.REAL, index = false)
    val horizontalAccuracy: Float? = null,

    @ColumnInfo(name= "verticalAccuracy", typeAffinity = ColumnInfo.REAL, index = false)
    val verticalAccuracy: Float? = null,

    @ColumnInfo(name= "speedAccuracy", typeAffinity = ColumnInfo.REAL, index = false)
    val speedAccuracy: Float? = null,

    @ColumnInfo(name= "bearingAccuracy", typeAffinity = ColumnInfo.REAL, index = false)
    val bearingAccuracy: Float? = null,

    @ColumnInfo(name= "isSimulated", typeAffinity = ColumnInfo.INTEGER, index = false)
    val isSimulated: Boolean
)
