package com.bungabear.databurner.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BurnTaskHistoryDao{
    @Query("SELECT * FROM BurnTaskHistory")
    fun getAll() : List<BurnTaskHistory>

    @Query("SELECT * FROM BurnTaskHistory WHERE timestamp >= :start AND timestamp <= :end")
    fun getBetweenTimestamp(start: Long, end: Long) : List<BurnTaskHistory>

    @Insert
    fun insert(history : BurnTaskHistory)
}