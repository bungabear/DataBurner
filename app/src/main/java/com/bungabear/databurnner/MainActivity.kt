package com.bungabear.databurnner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


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