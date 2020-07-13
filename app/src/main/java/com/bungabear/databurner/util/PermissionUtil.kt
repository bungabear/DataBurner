package com.bungabear.databurner.util

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.AppOpsManager.OnOpChangedListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process.myUid
import android.provider.Settings
import androidx.annotation.RequiresApi
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception

object PermissionUtil {
    const val REQUEST_READ_PHONE_STATE_CODE = 1000

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun checkUsageStatusPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.packageName)
        } else {
            appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun askUsageStatusPermission(context: Context, callbackActivity: Class<*>? = null){
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        appOps.startWatchingMode(OPSTR_GET_USAGE_STATS, context.packageName, object : OnOpChangedListener {
            override fun onOpChanged(op: String, packageName: String) {
                if(checkUsageStatusPermission(context)){
                    appOps.stopWatchingMode(this)
                    if (callbackActivity != null) {
                        // launch self
                        val intent = Intent(context, callbackActivity)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            }
        })
        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadPhoneStatePermission(context: Context) : Boolean {
        return context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun askReadPhoneStatePermission(activity: Activity) : Int {
        activity.requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE_CODE)
        return REQUEST_READ_PHONE_STATE_CODE
    }

    fun askRootPermission() : Boolean {
        val p: Process
        try {
            p = Runtime.getRuntime().exec("su")

            val os = DataOutputStream(p.outputStream)
            os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n")
            os.writeBytes("exit\n")
            os.flush()
            p.waitFor()
            if (p.exitValue() != 255) {
                return true
            }
        } catch (e: Exception ) {

        }
        return false
    }
}