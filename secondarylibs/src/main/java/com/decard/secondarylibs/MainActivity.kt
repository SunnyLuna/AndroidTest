package com.decard.secondarylibs

import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.decard.secondarylibs.presentation.MyDisplay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    private val TAG = "---MainActivity"
    private var mDisplay: Display? = null
    private var payDisplay: MyDisplay? = null

    //订阅权限申请
    private lateinit var mPermissionSubscribe: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPermission()
    }

    /**
     * 授权
     */
    private fun initPermission() {
        //申请权限和双屏异显权限
        mPermissionSubscribe = RxPermissions(this).request(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        ).subscribe {
            Log.d(TAG, "initPermission: $it")
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivityForResult(intent, 11)
                } else {
                    initData()
                }
            }
        }
    }

    /**
     * 副屏权限申请结果
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            initData()
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        //获取双屏异显的数据
        val manager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = manager.displays
        Log.d(TAG, "displays: " + displays.size)
        //0表示主屏，1表示副屏
        val display = displays[0]
        Log.d(TAG, "主屏分辨率: " + display.mode.physicalWidth + "*" + display.mode.physicalHeight)
        val display1 = displays[1]
        Log.d(TAG, "副屏分辨率: " + display1.mode.physicalWidth + "*" + display1.mode.physicalHeight)
        mDisplay = manager.displays[1]
        Log.d(TAG, "initData: ${manager.displays.size}")
        showPayDisplay()
    }


    /**
     * 副屏显示
     */
    private fun showPayDisplay() {
        if (mDisplay == null) return
        if (payDisplay == null) {
            payDisplay = MyDisplay(this, mDisplay)
            //TYPE_SYSTEM_OVERLAY   根据不同设备配置不同的弹框权限   p18f :TYPE_SYSTEM_OVERLAY  f11p: TYPE_APPLICATION_OVERLAY
            payDisplay!!.window!!.setType(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            )
            payDisplay!!.window!!.setWindowAnimations(R.style.presentationAnim)
        }
        if (!payDisplay!!.isShowing)
            payDisplay!!.show()
    }

}