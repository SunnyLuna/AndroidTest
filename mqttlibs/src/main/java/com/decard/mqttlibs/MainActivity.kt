package com.decard.mqttlibs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.mqttlibs.emqtt.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MessageCallback {

    private val TAG = "---MQTTService"
    private var serviceConnection: MyServiceConnection? = null
    private var mqttService: MQTTService? = null
    lateinit var mIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService()
        stService()
        btn_test.setOnClickListener {
            Thread {
                val heartResponseBean = HeartResponseBean()
                val list: MutableList<UserInfoEntity> = ArrayList()
                for (i in 1..5) {
                    val userInfo = UserInfoEntity()
                    userInfo.card_id = i.toString()
                    list.add(userInfo)
                }
                heartResponseBean.data = list
                val gson = Gson().toJson(heartResponseBean)
                MQTTService.publish(gson)
            }.start()
        }
    }


    private fun stService() {
        mIntent = Intent(this, MQTTService::class.java)
        startService(mIntent)
    }

    private fun bindService() {
        serviceConnection = MyServiceConnection()
        serviceConnection!!.setIGetMessageCallBack(this@MainActivity)
        mIntent = Intent(this, MQTTService::class.java)
        bindService(mIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)

        mqttService = serviceConnection!!.mqttService
    }

    override fun setMessage(message: String?) {
        tv_msg.text = message
        mqttService!!.createNotification(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: 退出应用")
        unbindService(serviceConnection!!)
        stopService(mIntent)
    }
}