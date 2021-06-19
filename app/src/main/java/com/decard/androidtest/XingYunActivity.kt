package com.decard.androidtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class XingYunActivity : AppCompatActivity() {
    private val TAG = "---XingYunActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xing_yun)
        val mak0 = "22222222222222222222222222222222"
        val enk0 = "11111111111111111111111111111111"
        val deviceNo = "330000000001"
        val iccid = "2020202020202020202020202020202020202020"
        val mak1 = "F40379AB9E0EC533F40379AB9E0EC533"
        val enk1 = "F40379AB9E0EC533F40379AB9E0EC533"
        val localIPAddress = NetUtil.getLocalIPAddress()
        Log.d(TAG, "onCreate: $localIPAddress.")
    }
}