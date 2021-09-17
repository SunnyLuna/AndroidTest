package com.decard.uilibs.recycler

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decard.uilibs.MyApp
import com.decard.uilibs.databinding.LayoutItemBinding
import com.example.commonlibs.utils.ToastUtils

class TestAdapter(var tradeList: List<Trade>) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {
    private var currentPosition: Int = 0

    class ViewHolder(var layoutItemBinding: LayoutItemBinding) :
        RecyclerView.ViewHolder(layoutItemBinding.root) {
        init {
            layoutItemBinding.setClickListener {
                ToastUtils.toast(MyApp.instance, "点击一行")
            }
            layoutItemBinding.setClickOrder {
                ToastUtils.toast(
                    MyApp.instance,
                    "点击订单号"
                )
            }
        }

        fun bind(item: Trade) {
            layoutItemBinding.trade = item
        }

    }

    override fun getItemCount(): Int {
        return tradeList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trade = tradeList[position]
        holder.bind(trade)

        if (currentPosition == position) {
            holder.layoutItemBinding.rlBack.setBackgroundColor(Color.parseColor("#337DD6"))
        } else {
            holder.layoutItemBinding.rlBack.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    private val TAG = "---TestAdapter"
    fun markCurrentPosition(selectedPosition: Int) {
        currentPosition = selectedPosition
        Log.d(TAG, "markCurrentPosition: $currentPosition")
        if (currentPosition == 0) {
            notifyItemChanged(currentPosition)
            notifyItemChanged(tradeList.size-1)
        } else {
            notifyItemChanged(currentPosition)
            notifyItemChanged(currentPosition - 1)
        }
    }
}