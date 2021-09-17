package com.decard.bluetoothlibs

import android.bluetooth.*
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Method


/**
 * 经典蓝牙
 * @author ZJ
 * created at 2021/7/27 14:51
 */
class ClassicActivity : AppCompatActivity() {

    private val TAG = "---ClassicActivity"
    lateinit var mBluetoothManager: BluetoothManager
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothA2dp: BluetoothA2dp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic)
        mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager.adapter
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "onCreate: 当前设备不支持蓝牙")
        } else {
            Log.d(TAG, "onCreate: 当前设备支持蓝牙")
        }
        mBluetoothAdapter!!.getProfileProxy(this, profileServiceListener, BluetoothProfile.A2DP)
        registerDiscoveryReceiver()
        registerA2dpReceiver()
        startScan()
    }

    private val profileServiceListener: ServiceListener = object : ServiceListener {
        override fun onServiceDisconnected(profile: Int) {
            Log.d(TAG, "onServiceDisconnected: ")
            if (profile == BluetoothProfile.A2DP) {
                Toast.makeText(
                    this@ClassicActivity,
                    "onServiceDisconnected",
                    Toast.LENGTH_SHORT
                ).show()
                bluetoothA2dp = null
            }
        }

        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            Log.d(TAG, "onServiceConnected: ")
            if (profile == BluetoothProfile.A2DP) {
                Toast.makeText(this@ClassicActivity, "onServiceConnected", Toast.LENGTH_SHORT)
                    .show()
                bluetoothA2dp = proxy as BluetoothA2dp
            }
        }
    }


    private fun registerDiscoveryReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(discoveryReceiver, intentFilter)
    }


    private val discoveryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED ->
                    Log.d(TAG, "onReceive: 正在搜索附近的蓝牙设备")
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d(TAG, "onReceive: 搜索结束")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val bluetoothDevice =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Log.d(TAG, "onReceive: ${bluetoothDevice!!.name}")
                    if (bluetoothDevice.name == "BT07") {
                        mBluetoothAdapter!!.cancelDiscovery()
                        connectA2dp(bluetoothDevice)
                    }
                }
            }
        }
    }

    private val a2dpReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                    val connectState = intent.getIntExtra(
                        BluetoothA2dp.EXTRA_STATE,
                        BluetoothA2dp.STATE_DISCONNECTED
                    )
                    if (connectState == BluetoothA2dp.STATE_DISCONNECTED) {
                        Toast.makeText(this@ClassicActivity, "已断开连接", Toast.LENGTH_SHORT).show()
                    } else if (connectState == BluetoothA2dp.STATE_CONNECTED) {
                        Toast.makeText(this@ClassicActivity, "已连接", Toast.LENGTH_SHORT).show()
                    }
                }
                BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED -> {
                    val playState = intent.getIntExtra(
                        BluetoothA2dp.EXTRA_STATE,
                        BluetoothA2dp.STATE_NOT_PLAYING
                    )
                    if (playState == BluetoothA2dp.STATE_PLAYING) {
                        Toast.makeText(this@ClassicActivity, "处于播放状态", Toast.LENGTH_SHORT)
                            .show()
                    } else if (playState == BluetoothA2dp.STATE_NOT_PLAYING) {
                        Toast.makeText(this@ClassicActivity, "未在播放", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun startScan() {
        if (!mBluetoothAdapter!!.isEnabled) {
            if (mBluetoothAdapter!!.enable()) {

//                handler.postDelayed(Runnable { scanDevice() }, 1500)
            } else {
                Toast.makeText(this, "请求蓝牙权限被拒绝，请授权", Toast.LENGTH_SHORT).show()
            }
        } else {
            scanDevice()
        }
    }

    private fun scanDevice() {
        if (mBluetoothAdapter!!.isDiscovering) {
            mBluetoothAdapter!!.cancelDiscovery()
        }
        mBluetoothAdapter!!.startDiscovery()
    }


    private fun registerA2dpReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)
        registerReceiver(a2dpReceiver, intentFilter)
    }

    private fun connectA2dp(bluetoothDevice: BluetoothDevice?) {
        if (bluetoothA2dp == null || bluetoothDevice == null) {
            Log.d(TAG, "connectA2dp: 连接为空")
            return
        }
        setPriority(bluetoothDevice, 100)
        try {
            val connectMethod: Method =
                BluetoothA2dp::class.java.getMethod("connect", BluetoothDevice::class.java)
            connectMethod.invoke(bluetoothA2dp, bluetoothDevice)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setPriority(device: BluetoothDevice?, priority: Int) {
        try {
            val connectMethod =
                BluetoothA2dp::class.java.getMethod(
                    "setPriority",
                    BluetoothDevice::class.java,
                    Int::class.javaPrimitiveType
                )
            connectMethod.invoke(bluetoothA2dp, device, priority)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}