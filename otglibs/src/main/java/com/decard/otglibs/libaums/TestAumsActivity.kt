package com.decard.otglibs.libaums

import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.otglibs.R
import com.github.mjdev.libaums.fs.UsbFile
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit

class TestAumsActivity : AppCompatActivity() {
    private val TAG = "---TestAumsActivity"
    private lateinit var usbHelper: UsbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_aums)
        usbHelper = UsbHelper(this, object : USBBroadCastReceiver.UsbListener {
            override fun insertUsb(device_add: UsbDevice?) {
                Log.d(TAG, "insertUsb: ")
                changeFile()
                Observable.timer(3, TimeUnit.SECONDS).subscribe {
                }
            }

            override fun getReadUsbPermission(usbDevice: UsbDevice?) {
                Log.d(TAG, "getReadUsbPermission: ")
            }

            override fun failedReadUsb(usbDevice: UsbDevice?) {
                Log.d(TAG, "failedReadUsb: ")
            }

            override fun removeUsb(device_remove: UsbDevice?) {
                Log.d(TAG, "removeUsb: ")
            }
        })


    }

    private fun changeFile() {
        val deviceList = usbHelper.deviceList
        Log.d(TAG, "devicelist: ${deviceList.size}")
        for (device in deviceList) {
            val readDevice = usbHelper.readDevice(device)
            for (usbFile in readDevice) {
                Log.d(TAG, "changeFile: ${usbFile.name}")
                if (usbFile.name == "123.TXT") {
                    Log.d(TAG, "changeFile: 开始复制")
                    saveUsbFileToSD(usbFile)
                    return
                }
            }
//            saveSDFileToUsb()
        }


    }

    private fun saveUsbFileToSD(usbFile: UsbFile) {
        Log.d(TAG, "saveUsbFileToSD: ")
        val sdpath =
            Environment.getExternalStorageDirectory().absolutePath + File.separator+"789.txt"
        usbHelper.saveUSbFileToLocal(
            usbFile, sdpath
        ) { progress -> Log.d(TAG, "downloadProgress: $progress") }
    }

    private fun saveSDFileToUsb() {
        val path =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "456.txt"
        val sdFile = File(path)
        if (sdFile.exists()) {
            Log.d(TAG, "changeFile: sdFile存在")
        } else {
            Log.d(TAG, "changeFile: sdFile不存在")
            return
        }
        val usbFile = usbHelper.currentFolder
        val name = usbFile.name
        Log.d(TAG, "usbFile.name: $name")
        usbHelper.saveSDFileToUsb(
            sdFile, usbFile
        ) { progress -> Log.d(TAG, "downloadProgress: $progress") }
    }
}