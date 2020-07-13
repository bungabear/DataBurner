package com.bungabear.databurner

import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bungabear.databurner.database.AppDatabase
import com.bungabear.databurner.databinding.ActivityMainBinding
import com.bungabear.databurner.downloader.BurnWorker
import com.bungabear.databurner.util.Config
import com.bungabear.databurner.util.NetworkUtil
import com.bungabear.databurner.util.PermissionUtil
import com.bungabear.databurner.util.cut
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val db by lazy { Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.DBName).build()}
    private var code = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding

        Config.DataUsage.observe(this, Observer {
            var usage = it / 1024.0 // kb
            usage /= 1024.0 // mb
            binding.dataUsage = "${usage.cut(2)}MB"
        })

        Config.WifiStatus.observe(this, Observer {
            when (it) {
                WifiManager.WIFI_STATE_ENABLING, WifiManager.WIFI_STATE_ENABLED -> {
                    binding.isWifiOn = true
                }
                else -> binding.isWifiOn = false
            }
        })

        binding.wifiSwitchListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            NetworkUtil.setWifi(this, isChecked)
        }

        Config.WifiStatus.value = NetworkUtil.isWifiOn(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (PermissionUtil.checkUsageStatusPermission(this)) {
                Config.DataUsage.value = NetworkUtil.getMobileNetworkUsage(this)
            }
            else {
                PermissionUtil.askUsageStatusPermission(this, MainActivity::class.java)
            }
        }
        else {
            // under lollipop
        }

        CoroutineScope(Dispatchers.IO).launch{
            val history = db.burnTaskHistoryDao().getAll()
            val historyStr = history.joinToString("\n") { it.toString() }
            withContext(Dispatchers.Main){
                binding.history = historyStr
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val data = Data.Builder()
                .putLong(BurnWorker.FILE_SIZE_KEY, 20 * 1024 * 1024)
                .build()

            val req = OneTimeWorkRequestBuilder<BurnWorker>()
                .setInputData(data)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(this).enqueue(req)
        }
    }

    override fun onResume() {
        super.onResume()
        if (PermissionUtil.checkUsageStatusPermission(this)) {
            Config.DataUsage.value = NetworkUtil.getMobileNetworkUsage(this)
        }
    }
}