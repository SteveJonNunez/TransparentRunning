package com.runningtechie.transparentrunning.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.runningtechie.transparentrunning.model.LocationPoint

@Dao
interface LocationPointDao: BaseDao<LocationPoint> {
    @Query("SELECT * from LocationPoint")
    fun getAllLocationPoints(): List<LocationPoint>

    @Query("SELECT * from LocationPoint WHERE sessionId=(:sessionId)")
    fun getAllLocationPoints(sessionId: Long): List<LocationPoint>?
}