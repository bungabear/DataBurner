package com.bungabear.databurnner

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bungabear.databurnner.database.AppDatabase
import com.bungabear.databurnner.database.dao.BurnTaskHistory
import com.bungabear.databurnner.network.FileDownloadRetrofit
import java.lang.Exception

class BurnWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams){

    private val db by lazy { Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.DBName).build() }

    override fun doWork(): Result {
        val size = inputData.getLong(FILE_SIZE_KEY, -1)
        if(size < 1){
            return Result.failure()
        }
        try{
            val result = FileDownloadRetrofit().download(size).execute()
            var realSize = 0L
            realSize += result.body()?.contentLength()?: 0L
            realSize += result.errorBody()?.contentLength()?: 0L
            Log.d("Worker", "download ${size} -> ${realSize}")
            db.burnTaskHistoryDao().insert(BurnTaskHistory(size = size, timestamp = System.currentTimeMillis()))
        } catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
    }

    companion object{
        const val FILE_SIZE_KEY = "FileSize"
    }

}