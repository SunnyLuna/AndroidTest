package com.decard.uilibs.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.decard.uilibs.R

/**
 *自定义圆环
 * @author ZJ
 * create at 2021/11/8 18:48
 */
public class CustomRing : View {

    private val TAG = "---CustomRing"
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRingColor = 0//圆环颜色
    private var mStartColor = 0//渐变开始颜色
    private var mEndColor = 0//渐变结束颜色
    private var mRingWidth = 2f//圆环宽度

    @Volatile
    private var mIsGradient = false//是否渐变
    private var currentAngle = 0f //当前旋转角度


    constructor(context: Context?) : this(context, null, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr, 0
    )

    @SuppressLint("Recycle", "CustomViewStyleable")
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        val typeArray =
            context!!.obtainStyledAttributes(attrs, R.styleable.MyRing, defStyleAttr, defStyleRes)
        mRingColor =
            typeArray.getColor(R.styleable.MyRing_ringColor, Color.parseColor("#E5E9F1"))
        mStartColor =
            typeArray.getColor(R.styleable.MyRing_startColor, Color.parseColor("#FFFFFF"))
        mEndColor =
            typeArray.getColor(R.styleable.MyRing_endColor, Color.parseColor("#008BFF"))
        mRingWidth = typeArray.getDimension(R.styleable.MyRing_ringWidth, 2f)
        mIsGradient = typeArray.getBoolean(R.styleable.MyRing_isGradient, false)
        startAnim()
    }

    fun setRingColor(color: Int) {
        Log.d(TAG, "setRingColor: $color")
        stopAnim()
        mRingColor = color
    }

    fun setRingColor(startColor: Int, endColor: Int) {
        Log.d(TAG, "setRingColor: $startColor  $endColor")
        mIsGradient = true
        mStartColor = startColor
        mEndColor = endColor
        startAnim()
    }


    private fun startAnim() {

        Thread {
            while (mIsGradient) {
                postInvalidate()
                SystemClock.sleep(20)
            }
            postInvalidate()
        }.start()
    }

    fun stopAnim() {
        mIsGradient = false
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.translate(width / 2f, height / 2f)
        if (mIsGradient) {
            //转起来
            canvas.rotate(currentAngle, 0f, 0f)
            if (currentAngle >= 360f) {
//            currentAngle -= 360f
                currentAngle = 0f
            } else {
                currentAngle += 2f
            }
        }

        val ringRadius = width / 2f
        val ringWidth = mRingWidth.toInt()
        //圆环外接矩形
        val rectF = RectF(
            -ringRadius + ringWidth / 2,
            -ringRadius + ringWidth / 2,
            ringRadius - ringWidth / 2,
            ringRadius - ringWidth / 2
        )
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.strokeWidth = ringWidth.toFloat()
        mPaint.style = Paint.Style.STROKE
        if (mIsGradient) {
            //圆环颜色
            val ringColors = intArrayOf(mStartColor, mEndColor)
            mPaint.shader = SweepGradient(0f, 0f, ringColors, null)
        } else {
            mPaint.color = mRingColor
        }
        canvas.drawArc(rectF, 0f, 360f, false, mPaint)


    }


    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    private fun measure(origin: Int): Int {
        var result = 200
        val specMode = MeasureSpec.getMode(origin)
        val specSize = MeasureSpec.getSize(origin)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

}