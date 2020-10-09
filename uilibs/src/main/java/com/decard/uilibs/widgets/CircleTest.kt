package com.decard.uilibs.widgets

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
/**
 * 自定义裁剪
 * @author ZJ
 * created at 2020/9/9 17:35
 */
class CircleTest : androidx.appcompat.widget.AppCompatButton {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //设置自定义的轮廓提供者来完成裁剪
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val rect =
                    Rect(0, 0, view.measuredWidth, view.measuredHeight)
                outline.setRoundRect(rect, view.measuredWidth / 2.toFloat())
            }
        }
        //开启组件的裁剪功能
        clipToOutline = true
    }
}