package com.decard.uilibs.md

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author ZJ
 * created at 2021/1/21 11:31
 *
 * 支持的key类型
 * Parcelable：任何Parcelable都可以用作selection的key，如果view中的内容与稳定的content:// uri相关联，你就使用uri作为你的key的类型
 * String：当基于字符串的稳定标识符可用时使用String
 * Long：当RecyclerView的long stable ID已经投入使用时，请使用Long，但是会有一些限制，在运行时访问一个稳定的id会被限定
 */
class MyItemDetailsLookup(var recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val holder = recyclerView.getChildViewHolder(view)
            if (holder is MaterialAdapter.MyViewHolder) {
                return holder.getItemDetails()
            }
        }
        return null
    }
}