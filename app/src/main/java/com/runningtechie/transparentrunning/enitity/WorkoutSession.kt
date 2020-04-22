package com.runningtechie.transparentrunning.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var title: String,
    val date: Date
)