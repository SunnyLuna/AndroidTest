package com.decard.mqttlibs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IGetMessageCallBack {

    private var serviceConnection: MyServiceConnection? = null
    private var mqttService: MQTTService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serviceConnection = MyServiceConnection()
        serviceConnection!!.setIGetMessageCallBack(this@MainActivity)

        val intent = Intent(this, MQTTService::class.java)

        bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)

        btn_test.setOnClickListener {
            MQTTService.publish("测试下")
        }
    }

    override fun setMessage(message: String?) {
        tv_msg.text = message
        mqttService = serviceConnection!!.mqttService
        mqttService!!.createNotification(message)
    }

    override fun onDestroy() {
        unbindService(serviceConnection!!)
        super.onDestroy()
    }
}