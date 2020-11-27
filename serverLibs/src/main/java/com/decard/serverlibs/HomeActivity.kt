package com.decard.serverlibs

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.net.InetAddress

class HomeActivity : AppCompatActivity() {
    private var ip: InetAddress? = null
    private val TAG = "---HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initNetWork()
    }

    private fun initNetWork() {
        val networkCallback = NetworkCallbackImpl(changeListener)
        val builder = NetworkRequest.Builder()
        val request = builder.build()
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.registerNetworkCallback(request, networkCallback)
    }

    private val changeListener = object : NetworkCallbackImpl.NetWorkChangeListener {
        override fun connect(i: Int) {
            if (NetUtils.isEth()) {
                startServer()
            }
        }

        override fun disConnect() {
            if (ip != null)
                ServerManager(ip!!).stopServer()
        }
    }

    fun startServer() {
        ip = NetUtils.getLocalIPAddress()
        Log.d(TAG, "startServer: $ip")
        if (ip == null) {
            runOnUiThread {
                Log.d(TAG, "startServer: 请连接以太网,并重启应用")
            }
        } else {
            Log.d("------homeActivity", "ip: " + ip!!.hostAddress)
            ServerManager(ip!!).startServer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (ip != null)
            ServerManager(ip!!).stopServer()
    }
}