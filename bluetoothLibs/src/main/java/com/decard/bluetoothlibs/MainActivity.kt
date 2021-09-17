package com.decard.bluetoothlibs

import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.reflect.Method
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TTSUtils.getInstance().init(this)
        TTSUtils.getInstance().setVol(15)
        val btnPlay = findViewById<Button>(R.id.btn_play)
        btnPlay.setOnClickListener {
            TTSUtils.getInstance().speak("测试下语音")
        }
        RxPermissions(this).request(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe {
                Log.d(TAG, "onCreate: $it")
                initBlueTooth()

            }

    }

    var bluetoothAdapter: BluetoothAdapter? = null
    private val REQUEST_ENABLE_BLUETOOTH = 1


    private fun initBlueTooth() {
        val intentFilter = IntentFilter() //创建一个IntentFilter对象
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND) //获得扫描结果
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED) //绑定状态变化
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED) //开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) //扫描结束
        registerReceiver(bluetoothReceiver, intentFilter) //注册广播接收器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() //获取蓝牙适配器

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter!!.isEnabled) { //打开
                //开始扫描周围的蓝牙设备,如果扫描到蓝牙设备，通过广播接收器发送广播
                bluetoothAdapter!!.startDiscovery()
            } else { //未打开
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH)
            }
        } else {
            Log.d(TAG, "initBlueTooth: 当前设备不支持蓝牙")
        }
    }

    private var bluetoothReceiver = object : BroadcastReceiver() {
        private val TAG = "---BluetoothReceiver"
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            Log.d(TAG, "onReceive: $action")
            when (action) {
                BluetoothDevice.ACTION_FOUND -> showDevicesData(context, intent) //数据展示
//                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> mAdapter.changeBondDevice() //刷新适配器
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> loadingLay.setVisibility(View.VISIBLE) //显示加载布局
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> loadingLay.setVisibility(View.GONE) //隐藏加载布局
            }
        }

        private fun showDevicesData(context: Context?, intent: Intent) {
            val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
            if (device.name != null) {
                Log.d(TAG, "showDevicesData: " + device.name)
                if (device.name == "BT07") {
                    if (bluetoothAdapter!!.isDiscovering) {
                        Log.d(TAG, "connect: 停止搜索")
                        bluetoothAdapter!!.cancelDiscovery()
                    }
                    Log.d(TAG, "showDevicesData:bondState:  ${device.bondState}")
                    if (device.bondState == BluetoothDevice.BOND_BONDED) {
                        Log.d(TAG, "showDevicesData: 已配对")
                        connect(device)
                    } else {
                        Log.d(TAG, "showDevicesData: 未配对")
                        //蓝牙配对
                        val method = BluetoothDevice::class.java.getMethod("createBond")
                        val invoke = method.invoke(device)
                        Log.d(TAG, "showDevicesData: invoke $invoke")
//                        connect(device)

//                        device.connectGatt(
//                            this@MainActivity,
//                            false,
//                            object : BluetoothGattCallback() {
//                                override fun onConnectionStateChange(
//                                    gatt: BluetoothGatt?,
//                                    status: Int,
//                                    newState: Int
//                                ) {
//                                    Log.d(TAG, "onConnectionStateChange:status " + status + "newState" + newState)
//                                    super.onConnectionStateChange(gatt, status, newState)
//                                }
//                            })
                    }
                }
            }
        }


        private fun connect(device: BluetoothDevice) {
            Thread {
                var mBluetoothSocket =
                    device.createRfcommSocketToServiceRecord(UUID.fromString("0003cdd2-0000-1000-8000-00805f9b0131"))


//                mBluetoothSocket = device.javaClass.getDeclaredMethod(
//                    "createRfcommSocket", *arrayOf<Class<*>?>(
//                        Int::class.javaPrimitiveType
//                    )
//                ).invoke(device, 1) as BluetoothSocket
                if (!mBluetoothSocket.isConnected) {
                    Log.d(TAG, "connect: 未连接")
                    Thread {
                        try {
                            mBluetoothSocket.connect()
                        } catch (e: Exception) {
                            Log.d(TAG, "connect: 首次连接失败" + e.message)
                            try {
                                val m: Method = device.javaClass.getMethod(
                                    "createRfcommSocket", Int::class.javaPrimitiveType
                                )
                                mBluetoothSocket =
                                    m.invoke(device, Integer.valueOf(1)) as BluetoothSocket
                                mBluetoothSocket.connect()
                            } catch (e: Exception) {
                                Log.d(TAG, "connect: 再次连接失败" + e.message)
                            }
                        }
                    }.start()
                }
            }.start()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                bluetoothAdapter!!.startDiscovery()
                Log.d(TAG, "蓝牙打开成功: ")
            } else {
                Log.d(TAG, "蓝牙打开失败: ")
            }
        }
    }
}