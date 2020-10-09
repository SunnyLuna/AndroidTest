package com.decard.uilibs.spinner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decard.uilibs.R

/**
 * 自定义spinner
 * @author ZJ
 * created at 2020/9/9 20:51
 */
class ZJSpinner : AppCompatTextView {

    private val TAG = "---ZJSpinner"
    private var mOnItemClickListener: SpinnerAdapter.OnClickListener? = null
    private lateinit var mPopup: PopupWindow
    private var mDrawable: Drawable? = null
    private lateinit var mRecyclerView: RecyclerView

    private val INSTANCE_STATE = "instance_state"
    private val SELECTED_INDEX = "selected_index"
    private val DATASET = "dataset"
    private val IS_POPUP_SHOWING = "is_popup_showing"
    private var mSelectedIndex = 0
    private val VIEW_LEFT = 0
    private val VIEW_TOP = 1
    private var mViewBounds: IntArray? = null
    private val MAX_LEVEL = 10000

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        Log.d(TAG, ": constrouctor")
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SunnySpinner)
        //设置微件的对齐方式
        gravity = Gravity.CENTER_VERTICAL or Gravity.START
        //是否可点击
        isClickable = true
        //设置背景
        setBackgroundResource(R.drawable.spinner_selector)
        val basicDrawable =
            ContextCompat.getDrawable(getContext(), R.drawable.arrow)
        val resId = typedArray.getColor(R.styleable.SunnySpinner_arrowTint, -1)

        if (basicDrawable != null) {
            mDrawable = DrawableCompat.wrap(basicDrawable)
            if (resId != -1) {
                DrawableCompat.setTint(mDrawable!!, resId)
            }
        }
        //自定义设置图标，可以上下左右设置对应的图标
        //setCompoundDrawables  必须先设置  setBounds 确定初始位置，宽高等界线信息
        //setCompoundDrawablesWithIntrinsicBounds 设置具有固定界线的图片
        //setCompoundDrawablesWithIntrinsicBounds(null,null,mDrawable,null)
        mDrawable!!.setBounds(20, 5, 75, 30)
        setCompoundDrawables(null, null, mDrawable, null)

        initPopWindow(context)
        typedArray.recycle()
    }

    /**
     * 创建自定义popupWindow
     */
    private fun initPopWindow(context: Context) {
        mPopup = PopupWindow(context)
        val popView = LayoutInflater.from(context).inflate(R.layout.spinner_pop, null)
        mRecyclerView = popView.findViewById(R.id.rv_spinner)
        mRecyclerView.layoutManager = LinearLayoutManager(popView.context)
        mPopup.contentView = popView
        //点击其他地方是否隐藏
        mPopup.isOutsideTouchable = true

        //设置弹窗的背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopup.elevation = 16f
            mPopup.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    getContext(),
                    R.drawable.spinner_drawable
                )
            )
        } else {
            mPopup.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    getContext(),
                    R.drawable.drop_down_shadow
                )
            )
        }

        //设置popup的拦截事件  当popwindow打开的时候，拦截点击按钮，避免出现多次按键无效的情况
        mPopup.setTouchInterceptor(OnTouchListener { view, event ->
            val x = event.rawX
            val y = event.rawY
            when (event.action) {
                MotionEvent.ACTION_OUTSIDE -> {
                    if (mViewBounds == null) {
                        mViewBounds = IntArray(2)
                        //获取当前View在屏幕上的位置，x和y的位置
                        getLocationInWindow(mViewBounds)

                    }
                    if (isTouchInsideViewBounds(
                            x,
                            y,
                            mViewBounds!!,
                            this
                        )
                        && mPopup.isShowing
                    ) {
                        Log.d(TAG, "initPopWindow: ")
                        return@OnTouchListener true
                    }
                    dismissDropDown()
                }
            }
            false
        })
    }

    /**
     * 判断是否点击View
     */
    private fun isTouchInsideViewBounds(
        x: Float,
        y: Float,
        viewBounds: IntArray,
        view: View
    ): Boolean {
        return x < viewBounds[VIEW_LEFT] + view.width && x > viewBounds[VIEW_LEFT]
                && y < viewBounds[VIEW_TOP] + view.height && y > viewBounds[VIEW_TOP]
    }

//    override fun onSaveInstanceState(): Parcelable? {
//        Log.d(TAG, "onSaveInstanceState: ")
//        val bundle = Bundle()
//        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
//        bundle.putInt(SELECTED_INDEX, mSelectedIndex)
//        bundle.putSerializable(DATASET, mDataList)
//        if (mPopup != null) {
//            bundle.putBoolean(IS_POPUP_SHOWING, mPopup.isShowing)
//            dismissDropDown()
//        }
//        return bundle
//    }
//
//    override fun onRestoreInstanceState(savedState: Parcelable?) {
//        Log.d(TAG, "onRestoreInstanceState: ")
//        var savedState = savedState
//        if (savedState is Bundle) {
//            val bundle = savedState
//            mSelectedIndex = bundle.getInt(SELECTED_INDEX)
//            mDataList = bundle.getSerializable(DATASET) as ArrayList<*>?
//            if (mDataList != null) {
//                val dataList = mDataList
//                text = dataList!![mSelectedIndex].toString()
//            }
//            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
//                if (mPopup != null) {
//                    // Post the show request into the looper to avoid bad token exception
//                    post { showDropDown() }
//                }
//            }
//            savedState = bundle.getParcelable(INSTANCE_STATE)
//        }
//        super.onRestoreInstanceState(savedState)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mPopup.width = MeasureSpec.getSize(widthMeasureSpec)
        mPopup.height = WindowManager.LayoutParams.WRAP_CONTENT
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent: ${event.action}  ${mPopup.isShowing}")
        if (event.action == MotionEvent.ACTION_UP) {
            if (!mPopup.isShowing) {
                showDropDown()
            } else {
                dismissDropDown()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun dismissDropDown() {
        animateArrow(false)
        mPopup.dismiss()
    }

    private fun showDropDown() {
        animateArrow(true)
        mPopup.showAsDropDown(this)
        Log.d(TAG, "showDropDown: ${mPopup.isShowing}")
    }


    /**
     * 设置箭头图标的动画
     */
    private fun animateArrow(shouldRotateUp: Boolean) {
        val start = if (shouldRotateUp) 0 else MAX_LEVEL
        val end = if (shouldRotateUp) MAX_LEVEL else 0
        val animator = ObjectAnimator.ofInt(mDrawable, "level", start, end)
        animator.interpolator = LinearOutSlowInInterpolator()
        animator.start()
    }

    private var mDataList: ArrayList<*>? = null
    private var mCurrent: String = ""
    fun <T> attachDataSource(dataList: ArrayList<T>?) {
        if (dataList != null) {
            mDataList = dataList
            val adapter = SpinnerAdapter(mDataList!!)
            mRecyclerView.adapter = adapter
            val dataList = mDataList
            text = dataList!![0].toString()
            adapter.setOnItemClickListener(object : SpinnerAdapter.OnClickListener {
                override fun onItemClick(msg: String) {
                    mCurrent = msg
                    Log.d(TAG, "onItemClick: $msg")
                    text = msg
                    dismissDropDown()
                }
            })
        }
    }

    fun getCurrentSpinner(): String {
        return mCurrent
    }

    fun addOnItemClickListener(onItemClickListener: SpinnerAdapter.OnClickListener) {
        mOnItemClickListener = onItemClickListener
    }
}