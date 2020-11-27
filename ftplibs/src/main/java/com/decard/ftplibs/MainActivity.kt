package com.decard.ftplibs

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "FTP---MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_click.setOnClickListener {
            val intent = packageManager
                .getLaunchIntentForPackage("com.decard.mqttlibs")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Thread {
            Log.d(TAG, "onResume: 开始下载")
            val path = Environment.getExternalStorageDirectory().absolutePath
            Log.d(TAG, "onResume: $path")
            val downloadFile = FtpUtils.downloadFile(
                "192.168.7.113",
                21,
                "Sunny",
                "SunnyLuna",
                "E:\\WorkSpace\\FTPServer\\y.jpg",
                "y.jpg",
                path
            )
            Log.d(TAG, "onCreate: $downloadFile")
//            connectToServer()
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}