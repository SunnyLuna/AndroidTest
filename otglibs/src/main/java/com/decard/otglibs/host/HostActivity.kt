package com.decard.otglibs.host

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.otglibs.R
import java.io.IOException

/**
 *
 * @author ZJ
 * created at 2020/9/25 16:48
 *UsbManager 	您可以枚举连接的 USB 设备并与之通信。
 *UsbDevice 	表示连接的 USB 设备，并包含用于访问其标识信息、接口和端点的方法。
 *UsbInterface 	表示 USB 设备的接口，它定义设备的一组功能。设备可以具有一个或多个用于通信的接口。
 *UsbEndpoint 	表示接口端点，是此接口的通信通道。一个接口可以具有一个或多个端点，并且通常具有用于与设备进行双向通信的输入和输出端点。
 *UsbDeviceConnection 	表示与设备的连接，可在端点上传输数据。借助此类，您能够以同步或异步方式反复发送数据。
 *UsbRequest 	表示通过 UsbDeviceConnection 与设备通信的异步请求。
 *UsbConstants 	定义与 Linux 内核的 linux/usb/ch9.h 中的定义相对应的 USB 常量。
 *
 * 1：先通过usbDevice来获取UsbInterface
 *2：然后通过UsbInterface来获取UsbEndPoint类型，注意对应的收发的文件描述符
 *3：打开设备usbManager.openDevice(usbDevice);获取连接类型
 *4：建立连接与接口之间的关系connection.claimInterface(usbInterface, true);
 *5：通过connection来发送数据类型
 */
class HostActivity : AppCompatActivity() {
    private val TAG = "---HostActivity"
    lateinit var mUsbManager: UsbManager
    lateinit var mPermissionIntent: PendingIntent

    companion object {
        private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        init()
    }

    private fun init() {
        //USB管理器
        mUsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        mPermissionIntent =
            PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
        //注册广播,监听USB插入和拔出
        val intentFilter = IntentFilter()
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        registerReceiver(mUsbReceiver, intentFilter)
        //注册监听自定义广播
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(mUsbReceiver, filter)
    }

    private val mUsbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive: ${intent.action}")
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            Log.d(TAG, "version: ${device.version}")
                            Log.d(TAG, "deviceName: ${device.deviceName}")
                            Log.d(TAG, "productId: ${device.productId}")
                            Log.d(TAG, "manufacturerName: ${device.manufacturerName}")
                            Log.d(TAG, "vendorId: ${device.vendorId}")
                            Log.d(TAG, "interfaceCount: ${device.interfaceCount}")
                            for (i in 0..device.interfaceCount) {
                                val point = device.getInterface(i)
                                Log.d(TAG, "endpointCount: ${point.endpointCount}")
                                if (point.endpointCount == 2) {
                                    val usbDeviceConnection = mUsbManager.openDevice(device)
                                    //声称拥有对UsbInterface的独占访问权。
                                    val claim = usbDeviceConnection.claimInterface(
                                        device.getInterface(0),
                                        true
                                    )
                                    Log.d(TAG, "claim: $claim")
                                    if (!claim) {
                                        throw IOException("could not claim interface!")
                                    }
                                    Thread {
                                        val bytes = ByteArray(512)
                                        val bulkTransfer = usbDeviceConnection.bulkTransfer(
                                            point.getEndpoint(0),
                                            bytes,
                                            512,
                                            10
                                        )
                                    }.start()
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "permission denied for device $device")
                    }
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                device?.apply {


                }
            }
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED == intent.action) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                device?.apply {
                    mUsbManager.requestPermission(device, mPermissionIntent)

                }
            }
        }
    }

}