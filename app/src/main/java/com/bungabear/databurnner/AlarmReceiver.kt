package com.bungabear.databurnner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

    }

    companion object{
        const val ACTION_BURN = "ACTION_BURN"
        const val INTENT_TRAFFIC_SIZE = "TRAFFIC_SIZE"
    }
}