package com.decard.socketlibs

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.socketlibs.adb.ADBSocketManager
import kotlinx.android.synthetic.main.activity_a_d_b_socket.*
import java.nio.charset.Charset
/**
 * adb socket通信，本质上还是tcp通信
 *安卓端打开开发者模式，创建socket服务，
 * pc端安装adb 并输入  adb forward tcp:8000 tcp:9000      把PC电脑端TCP端口8000的数据转发到与电脑通过adb连接的Android设备的TCP端口9000上
 * 通过socket 工具输入 127.0.0.1   8000  进行连接通信
 *
 * @author ZJ
 * created at 2021/3/12 11:07
 */
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