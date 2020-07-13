package com.bungabear.databurner.downloader

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bungabear.databurner.database.AppDatabase
import com.bungabear.databurner.database.dao.BurnTaskHistory
import com.bungabear.databurner.util.NetworkUtil
import java.lang.Exception

class BurnWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams){

    private val db by lazy { Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.DBName).build() }

    override fun doWork(): Result {
        val size = inputData.getLong(FILE_SIZE_KEY, -1)
        if(size < 1){
            return Result.failure()
        }
        val wifiState = NetworkUtil.isWifiOn(applicationContext)
        try{
            if (wifiState != WifiManager.WIFI_STATE_DISABLED) {
                NetworkUtil.setWifi(applicationContext, false)
            }

            Thread.sleep(5000)

            val result = FileDownloadRetrofit().download(size).execute()
            var realSize = 0L
            realSize += result.body()?.contentLength()?: 0L
            realSize += result.errorBody()?.contentLength()?: 0L
            Log.d("Worker", "download $size -> $realSize")
            db.burnTaskHistoryDao().insert(BurnTaskHistory(size = size, timestamp = System.currentTimeMillis()))
        } catch (e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
        if (wifiState == WifiManager.WIFI_STATE_ENABLED || wifiState == WifiManager.WIFI_STATE_ENABLING) {
            NetworkUtil.setWifi(applicationContext, true)
        }
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
    }

    companion object{
        const val FILE_SIZE_KEY = "FileSize" // bytes
    }

}