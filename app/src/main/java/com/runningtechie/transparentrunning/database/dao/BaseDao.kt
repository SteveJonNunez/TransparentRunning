package com.runningtechie.transparentrunning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(t: T): Long

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)
}
