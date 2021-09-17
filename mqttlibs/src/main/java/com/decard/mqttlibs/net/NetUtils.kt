package com.decard.mqttlibs.net

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.mqttlibs.emqtt.MQTTApplication
import java.io.IOException


/**
 * 网络工具类
 * @author ZJ
 * created at 2020/10/21 11:54
 * ConnectivityManager：回应关于网络连接状态的查询。当网络连接发生变化时，它还会通知应用。
 * NetworkInfo：描述指定类型的网络接口的状态（目前为移动网络或 WLAN）。
 */
object NetUtils {

    private val TAG = "---NetUtils"
    private var networkChangeReceiver: NetworkStateReceiver? = null
    private var mContext: Context? = null
    /**
     * 检查网络接口是否可用
     */
    fun isOnLine(): Boolean {
        val connMgr =
            MQTTApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        Log.d(TAG, "isOnLine: " + networkInfo?.isConnected)
        Log.d(TAG, "isOnLine: " + networkInfo?.isAvailable)
        return networkInfo?.isConnected == true
    }

    /**
     * 检查网络接口是否可用
     */
    fun isOnline(): Boolean {
        val connectivityManager =
            MQTTApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.d(TAG, "isOnline: " + android.os.Build.VERSION.SDK_INT)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val networkCapabilities: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)!!
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("ping -c 3 www.baidu.com")
                val exitValue = ipProcess.waitFor()
                Log.i(TAG, "Process:$exitValue")
                return exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 获取当前网络类型
     *
     * @return NetType
     */
    @JvmStatic
    fun getNet(): String {
        val connMgr =
            MQTTApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo ?: return "NULL"
        return when (networkInfo.type) {
            ConnectivityManager.TYPE_MOBILE -> if ("cmnet" == networkInfo.extraInfo.toLowerCase()) {
                "CMNET"
            } else {
                "CMWAP"
            }
            ConnectivityManager.TYPE_WIFI -> "WIFI"
            ConnectivityManager.TYPE_ETHERNET -> "ETH"
            else -> "NULL"
        }
    }


    /**
     * 绑定监听网络状态
     */
    fun bindNetStatus(context: Context, listener: NetworkChangeListener) {
        mContext = context
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            NetworkCallbackImpl(listener)
//        } else {
            // 创建 IntentFilter 实例
            val intentFilter = IntentFilter()
            // 添加广播值
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            // 创建 NetworkChangeReceiver 实例
            networkChangeReceiver = NetworkStateReceiver(listener)
            // 注册广播
            mContext!!.registerReceiver(networkChangeReceiver, intentFilter)
//        }
    }

    /**
     * 解除网络状态的监听
     */
    fun unBindNetStatus() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            mContext!!.unregisterReceiver(networkChangeReceiver)
        }
    }


    /**
     * 判断是否是wifi连接
     */
    fun isWifi(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false

        return cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI

    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: AppCompatActivity) {
        val intent = Intent("/")
        val cm = ComponentName(
            "com.android.settings",
            "com.android.settings.WirelessSettings"
        )
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }
}
