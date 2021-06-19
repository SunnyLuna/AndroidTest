package com.decard.secondarylibs.presentation

import android.app.Presentation
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import com.decard.secondarylibs.CustomPopWindow
import com.decard.secondarylibs.R
import com.example.commonlibs.widgets.CustomPassword
import kotlinx.android.synthetic.main.presentation_my.*

/**
 * 支付副屏显示界面
 * @author ZJ
 * created at 2020/6/30 20:33
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class MyDisplay(outerContext: Context?, display: Display?) :
    Presentation(outerContext, display) {
    private val TAG = "---MyDisplay"
    private var updateWindow: CustomPopWindow? = null

    private val handler = Handler(Looper.getMainLooper())
    private var mWindowManager: WindowManager? = null
    private var phoneView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(context),
//            R.layout.presentation_pay,
//            null,
//            false
//        )
        setContentView(R.layout.presentation_my)
        iv_img.setOnClickListener {
//            showUpdate()
            inputPhoneNumber()
        }
    }


    /**
     * 显示数据更新的弹框
     */
    private fun showUpdate() {
        Log.d(TAG, "showUpdate: ")
        val updateView =
            LayoutInflater.from(context).inflate(R.layout.layout_update, null)
        if (updateWindow != null && updateWindow!!.isShowing()) {
            updateWindow!!.dismiss()
        }
        updateWindow = CustomPopWindow.PopWindowBuilder(context)
            .setOutSideTouchable(false)
            .setFocusable(false)
            .setView(updateView)
            .create()
            .showAtLocation(cl_my, Gravity.FILL, 0, 0)
    }

    /**
     * 输入手机号后四位的弹框
     */
    private fun inputPhoneNumber() {
        handler.post {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            phoneView =
                LayoutInflater.from(context).inflate(R.layout.layout_popup_keyboard, null)
            mWindowManager!!.addView(phoneView!!, getLayout())
            val passwordView = phoneView!!.findViewById(R.id.idPassword) as CustomPassword
            passwordView.setOnPasswordInputFinish {
                Log.d(TAG, "=============>$it")
                if (it.length == 4) {
                    removeWindowView()
                }
            }
            passwordView.cancelImageView.setOnClickListener {
                removeWindowView()
            }
        }
    }

    /**
     * 加载windowmanager的布局
     */
    private fun getLayout(): ViewGroup.LayoutParams {
        val mParams = WindowManager.LayoutParams()
        //TYPE_SYSTEM_OVERLAY   根据不同设备配置不同的弹框权限   p18f :TYPE_SYSTEM_OVERLAY  f11p: TYPE_APPLICATION_OVERLAY
        //TYPE_SYSTEM_ALERT
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        mParams.format = PixelFormat.TRANSLUCENT
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.gravity = Gravity.CENTER
        return mParams
    }

    /**
     * 移除手机号输入的弹框
     */
    private fun removeWindowView() {
        mWindowManager!!.removeView(phoneView)
        phoneView = null
        mWindowManager = null
    }

}