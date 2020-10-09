package com.decard.uilibs.widgets

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.decard.uilibs.R

/**
 * Toast工具类
 * 0成功
 * 1失败
 * 2提示
 * 其他 不显示
 * @author ZJ
 * created at 2020/7/24 10:40
 */
object MyToast {
    private val mHandler = Handler()
    private val canceled = true


    private var mToast: Toast? = null
    fun show(
        context: Context?,
        msg: String?,
        type: Int
    ) {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val ivToast =
            view.findViewById<ImageView>(R.id.iv_toast)
        when (type) {
            0 -> {
                ivToast.setImageBitmap(
                    BitmapFactory.decodeResource(
                        context!!.resources,
                        R.mipmap.ic_success
                    )
                )
            }
            1 -> {
                ivToast.setImageBitmap(
                    BitmapFactory.decodeResource(
                        context!!.resources,
                        R.mipmap.ic_fail
                    )
                )
            }
            2 -> {
                ivToast.setImageBitmap(
                    BitmapFactory.decodeResource(
                        context!!.resources,
                        R.mipmap.ic_warnning
                    )
                )
            }
        }
        //自定义toast文本
        val mTextView =
            view.findViewById<View>(R.id.tv_toast) as TextView
        mTextView.text = msg
        if (mToast == null) {
            mToast = Toast(context)
        }
        //设置toast居中显示
        mToast!!.setGravity(Gravity.CENTER, 0, 0)
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.view = view
        mToast!!.show()
    }

    /**
     * 隐藏toast
     */
    fun hide() {
        if (mToast != null) {
            mToast!!.cancel()
        }
        //        canceled = true;
        Log.i("ToastUtil", "Toast that customed duration hide...")
    }

    private fun showUntilCancel() {
        if (canceled) { //如果已经取消显示，就直接return
            return
        }
        mToast!!.show()
        mHandler.postDelayed({
            showUntilCancel()
        }, Toast.LENGTH_LONG.toLong())
    }

}