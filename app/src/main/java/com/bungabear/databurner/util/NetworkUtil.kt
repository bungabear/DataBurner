package com.bungabear.databurner.util

import android.Manifest
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.lang.Exception
import java.util.*

object NetworkUtil {

    /**
     * 모바일 데이터 사용량 읽어오는 API.
     * M 미만은 TrafficStats를 누계하여야 한다.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE])
    fun getMobileNetworkUsage(context: Context) : Long{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DATE, 1)

        val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val bucket = networkStatsManager.querySummaryForDevice(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            getSubscriberId(context),
            calendar.timeInMillis,
            System.currentTimeMillis()
        )
        return bucket.rxBytes + bucket.txBytes
    }

    /**
     * Android 10 미만은 네트워크 사용량 조회시 SubscribeId가 필요하다.
     * SubscribeId 조회를 위해서는 READ_PHONE_STATE 권한이 필요하다.
     * [PermissionUtil.askUsageStatusPermission], [PermissionUtil.checkReadPhoneStatePermission]
     *
     */
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE])
    private fun getSubscriberId(context: Context): String? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.subscriberId
        } else {
            null
        }
    }

    fun isWifiOn(context: Context) : Int {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.wifiState
    }

    fun setWifi(context: Context, isOn: Boolean) : Int{
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.isWifiEnabled = isOn
        }
        else {
            if(PermissionUtil.askRootPermission()){
                try {
                    val p = Runtime.getRuntime().exec("su -c svc wifi ${if (isOn) "enable" else "disable"}")
                    p.waitFor()
                } catch (e: Exception) {

                }
            }
            else {
                Toast.makeText(context, "Wifi 제어가 불가능합니다.",Toast.LENGTH_LONG).show()
                // not available.
            }
        }
        return isWifiOn(context)
    }
}