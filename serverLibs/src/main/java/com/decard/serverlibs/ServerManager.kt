package com.decard.serverlibs

import android.util.Log
import com.yanzhenjie.andserver.AndServer
import com.yanzhenjie.andserver.Server
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class ServerManager(ip: InetAddress) {

    private val TAG = "---ServerManager"
    private val mServer: Server

    init {
        mServer = AndServer.serverBuilder(MyApp.getAppContext())
            .inetAddress(ip)
            .port(8080).timeout(10, TimeUnit.SECONDS)
            .listener(object : Server.ServerListener {
                override fun onStarted() {
                    Log.d(TAG, "onStarted: ")
                }

                override fun onStopped() {
                    Log.d(TAG, "onStopped: ")
                }

                override fun onException(e: Exception) {
                    Log.d(TAG, "onException: ")
                }
            }).build()
    }

    fun startServer() {
        if (mServer.isRunning) {
            Log.d(TAG, "The server has started ")
        } else {
            mServer.startup()
        }
    }

    fun stopServer() {
        if (mServer.isRunning) {
            mServer.shutdown()
        } else {
            Log.d(TAG, "The server has not started yet.")
        }
    }


}
