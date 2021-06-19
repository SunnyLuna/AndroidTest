package com.decard.facelibrary.utils

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.util.AttributeSet
import android.view.TextureView
import android.view.View
import android.view.ViewOutlineProvider

/**
 * 自定义圆形预览
 * @author ZJ
 * created at 2020/7/10 15:20
 */
class MyTextureView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureView(context!!, attrs, defStyleAttr, 0) {
    init {
        //设置自定义的轮廓提供者来完成裁剪
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(
                view: View,
                outline: Outline
            ) {
                val rect =
                    Rect(0, 0, view.measuredWidth, view.measuredHeight)
                outline.setRoundRect(rect, view.measuredHeight / 2.toFloat())
            }
        }
        //开启组件的裁剪功能
        clipToOutline = true
    }
}