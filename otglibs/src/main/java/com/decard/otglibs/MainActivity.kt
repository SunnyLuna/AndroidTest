package com.decard.otglibs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.otglibs.utils.UDiskUtils

class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"

    private var isreadUDisk = false
    private var uPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUdisk()
    }

    /**
     * 初始化U盘
     */
    private fun initUdisk() {
        val usbPaths = UDiskUtils.getUSBPaths(this)
        Log.d(TAG, "initUdisk: ${usbPaths.size}")
        for (path in usbPaths.indices) {
            uPath = usbPaths[path]
            val mounted = UDiskUtils.isMounted(usbPaths[path])
            isreadUDisk = mounted
            Log.d(TAG, "initUdisk: ${usbPaths[path]}")
        }
        registerUDiskReceiver()
    }

    /**
     * 注册U盘
     */
    private fun registerUDiskReceiver() {
        //监听otg插入 拔出
        val usbDeviceStateFilter = IntentFilter()
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        usbDeviceStateFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED)
        usbDeviceStateFilter.addDataScheme("file")
        registerReceiver(mReceiver, usbDeviceStateFilter)
    }


    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: ${intent!!.action}")
            if (intent.action == Intent.ACTION_MEDIA_MOUNTED) {
                isreadUDisk = true
                uPath = intent.data!!.path!!
                Log.d(TAG, "路径存在$uPath")

            } else if (intent.action == Intent.ACTION_MEDIA_UNMOUNTED) {
                Log.d(TAG, "u盘拔出")
                isreadUDisk = false
            }
        }
    }
}