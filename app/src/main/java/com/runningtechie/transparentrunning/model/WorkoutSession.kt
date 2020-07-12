package com.runningtechie.transparentrunning.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "WorkoutSession")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id", typeAffinity = ColumnInfo.INTEGER, index = true)
    val id: Long? = null,

    @ColumnInfo(name= "title", typeAffinity = ColumnInfo.TEXT, index = false)
    var title: String,

    @ColumnInfo(name= "date", typeAffinity = ColumnInfo.INTEGER, index = false)
    val date: Date,

    @ColumnInfo(name= "duration", typeAffinity = ColumnInfo.INTEGER, index = false)
    val duration: Duration? = null,

    @ColumnInfo(name= "distance", typeAffinity = ColumnInfo.REAL, index = false)
    val distance: Distance? = null
)
