package com.bungabear.databurnner

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bungabear.databurnner.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val tvMain by lazy { findViewById<TextView>(R.id.tv_main) }
    private val db by lazy { Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.DBName).build()}
    private var code = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        CoroutineScope(Dispatchers.IO).launch{
            val history = db.burnTaskHistoryDao().getAll()
            val historyStr = history.joinToString("\n") { it.toString() }
            withContext(Dispatchers.Main){
                tvMain.text = historyStr
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val data = Data.Builder()
                .putLong(BurnWorker.FILE_SIZE_KEY, 100 * 1024 * 1024)
                .build()

            val req = OneTimeWorkRequestBuilder<BurnWorker>()
                .setInputData(data)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(this).enqueue(req)
//            val alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            val intent = Intent("ACTION_BURN")
//            intent.`package` = packageName
//            intent.putExtra(AlarmReceiver.INTENT_TRAFFIC_SIZE, 1000000)
//            val pendingIntent = PendingIntent.getBroadcast(this, code++, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            alarmManager?.set(
//                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 1000,
//                pendingIntent
//            )
        }
    }
}