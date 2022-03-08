package com.decard.androidtest.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition


/**
 *自定义view，直接替换布局的背景图片
 * @author ZJ
 * create at 2021/12/16 19:41
 */
class MyLayout : LinearLayout {
    private var viewTarget: CustomViewTarget<MyLayout, Drawable>? = null

    constructor(context: Context?) : this(context, null, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        viewTarget = object : CustomViewTarget<MyLayout, Drawable>(this) {
            override fun onLoadFailed(errorDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val myLayout = getView()
                myLayout.setImageAsBackground(resource)
            }

            override fun onResourceCleared(placeholder: Drawable?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun setImageAsBackground(resource: Drawable) {
        background = resource
    }


    fun getTarget(): CustomViewTarget<MyLayout, Drawable>? {
        return viewTarget
    }

}