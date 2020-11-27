package com.decard.mqttlibs.net

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.mqttlibs.R

/**
 *
 * @author ZJ
 * created at 2020/10/20 10:52
 * <uses-permission android:name="android.permission.INTERNET" />
 *<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
class NetworkActivity : AppCompatActivity() {

    private val TAG = "---NetworkActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
//        var isWifiConn: Boolean = false
//        var isMobileConn: Boolean = false
//
//        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connMgr.allNetworks.forEach { network ->
//            connMgr.getNetworkInfo(network).apply {
//                if (type == ConnectivityManager.TYPE_WIFI) {
//                    isWifiConn = isWifiConn or isConnected
//                }
//                if (type == ConnectivityManager.TYPE_MOBILE) {
//                    isMobileConn = isMobileConn or isConnected
//                }
//            }
//        }
        NetUtils.bindNetStatus(this,changeListener)
    }


    private val changeListener = object : NetworkChangeListener {
        override fun connect(i: Int) {
            Log.d(TAG, "connect: $i")
        }

        override fun disConnect() {
            Log.d(TAG, "disConnect: ")
        }
    }
}