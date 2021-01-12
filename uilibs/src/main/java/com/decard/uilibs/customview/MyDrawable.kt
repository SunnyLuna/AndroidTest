package com.decard.uilibs.customview

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log


public class MyDrawable : Drawable() {
    private val TAG = "---MyDrawable"
    private val mPaint = Paint().apply {
        color = Color.RED
    }

    override fun draw(canvas: Canvas) {
        val width: Int = bounds.width()
        val height: Int = bounds.height()
        Log.d(TAG, "draw: $width")
        Log.d(TAG, "draw: $height")
        val radius: Float = Math.min(width, height).toFloat() / 2f
        // Draw a red circle in the center
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, mPaint)

    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}