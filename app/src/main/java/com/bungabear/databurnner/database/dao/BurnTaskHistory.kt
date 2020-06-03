package com.bungabear.databurnner.database.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BurnTaskHistory(
    @PrimaryKey(autoGenerate = true) val index: Int? = null,
    @ColumnInfo val size: Long,
    @ColumnInfo val timestamp: Long
)