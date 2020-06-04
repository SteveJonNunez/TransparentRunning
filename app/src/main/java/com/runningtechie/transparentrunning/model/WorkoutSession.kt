package com.runningtechie.transparentrunning.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "WorkoutSession")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var title: String,
    val date: Date,
    val duration: Int? = null,
    val distance: Distance? = null
)
