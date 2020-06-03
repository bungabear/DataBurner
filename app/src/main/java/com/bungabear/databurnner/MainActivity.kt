package com.bungabear.databurnner

import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.Context
import android.net.TrafficStats
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppOpsManagerCompat.MODE_ALLOWED
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            val trafficBytes = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes()
//            Snackbar.make(view, "${trafficBytes/1024/1024}MBytes", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }

    }

}