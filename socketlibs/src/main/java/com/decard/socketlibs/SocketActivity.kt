package com.decard.socketlibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.socketlibs.port.IPort
import com.decard.socketlibs.port.tcp.SocketPort

class SocketActivity : AppCompatActivity() {
    private val TAG = "---SocketActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)
        Thread {

            val iPort = SocketPort.getInstance() as IPort
            val open = iPort.open("192.168.7.100", "4700")
            Log.d(TAG, "open: $open")
            val data = "日出东海荡西山"
            val isSend = iPort.send(data.toByteArray(), 0, data.toByteArray().size)
            Log.d(TAG, "isSend: $isSend")
//            while (true) {
            val temp = ByteArray(1024)
            val read = iPort.read(temp, temp.size)
            Log.d(TAG, "read: $read")
//            }
        }.start()


    }
}