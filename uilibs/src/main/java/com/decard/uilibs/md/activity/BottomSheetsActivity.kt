package com.decard.uilibs.md.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.decard.uilibs.R

/**
 * 底部动作条(Bottom Sheets)是一个从屏幕底部边缘向上滑出的一个面板，
 * 使用这种方式向用户呈现一组功能。底部动作条呈现了简单、清晰、无需额外解释的一组操作
 */
class BottomSheetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheets)
    }
}