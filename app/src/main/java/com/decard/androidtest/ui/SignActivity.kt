package com.decard.androidtest.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.decard.androidtest.R
import com.sr.signture.Slate
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    var type = 1// 0普通签名  1：电磁签名

    private var mSlate: Slate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        if (type == 1) {
            signPad.visibility = View.GONE
            mSlate = Slate(this)
            val root =
                findViewById<View>(R.id.rl_root) as ViewGroup
            root.addView(mSlate, 0)
            mSlate!!.setZoomMode(false)
            mSlate!!.setPenSize(0.5f, 10f)
            mSlate!!.setPenColor(Color.BLACK)//设置画笔颜色
            mSlate!!.setPenType(Slate.SHAPE_CIRCLE)//设置画笔类型
        }
        btn_confirm.setOnClickListener {
            val bitmap = if (type == 0) {
                signPad.signatureBitmap
            } else {
                mSlate!!.copyBitmap(false)
            }
            iv_sign.setImageBitmap(bitmap)
        }
        btn_retry.setOnClickListener {
            if (type == 0) {
                signPad.clear()
            } else {
                mSlate!!.clear()
            }
        }
    }
}