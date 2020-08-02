package com.runningtechy.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.runningtechy.database.model.LocationPoint

@Dao
interface LocationPointDao : BaseDao<LocationPoint> {
    @Query("SELECT * from LocationPoint")
    fun getAllLocationPoints(): LiveData<List<LocationPoint>>

    @Query("SELECT * from LocationPoint WHERE sessionId=(:sessionId) ORDER BY time ASC")
    fun getAllLocationPoints(sessionId: Long): LiveData<List<LocationPoint>>
}
