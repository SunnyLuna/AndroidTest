package com.decard.socketlibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.socketlibs.adb.ADBSocketManager
import kotlinx.android.synthetic.main.activity_a_d_b_socket.*
import java.nio.charset.Charset

class ADBSocketActivity : AppCompatActivity() {
    private val TAG = "---ADBSocketActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_d_b_socket)
        ADBSocketManager.instance().createServer {
            Log.d(TAG, "onCreate: ${String(it, Charset.forName("GBK"))}")
        }
        btn_send.setOnClickListener {
            ADBSocketManager.instance().sendData("发送".toByteArray(Charset.forName("gbk")))
        }
    }
}