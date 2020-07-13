package com.bungabear.databurner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bungabear.databurner.database.dao.BurnTaskHistory
import com.bungabear.databurner.database.dao.BurnTaskHistoryDao

@Database(entities = [BurnTaskHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun burnTaskHistoryDao(): BurnTaskHistoryDao

    companion object{
        const val DBName = "AppDatabase"
    }
}