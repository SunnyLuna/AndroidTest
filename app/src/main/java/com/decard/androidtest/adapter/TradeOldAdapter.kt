package com.decard.androidtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decard.androidtest.MyApplication
import com.decard.androidtest.databinding.LayoutItemBinding
import com.decard.androidtest.db.Trade
import com.example.commonlibs.utils.ToastUtils

class TradeOldAdapter(var tradeList: List<Trade>) :
    RecyclerView.Adapter<TradeOldAdapter.ViewHolder>() {


    class ViewHolder(var layoutItemBinding: LayoutItemBinding) :
        RecyclerView.ViewHolder(layoutItemBinding.root) {
        init {
            layoutItemBinding.setClickListener {
                ToastUtils.toast(MyApplication.instance, "点击一行")
            }
            layoutItemBinding.setClickOrder {
                ToastUtils.toast(
                    MyApplication.instance,
                    "点击订单号" + layoutItemBinding.trade!!.out_trade_no
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
    }
}