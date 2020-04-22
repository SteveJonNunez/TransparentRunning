package com.runningtechie.transparentrunning.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class WorkoutSessionWithLocationPoints(
    @Embedded val user: WorkoutSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val locationPoints: List<LocationPoint>
)