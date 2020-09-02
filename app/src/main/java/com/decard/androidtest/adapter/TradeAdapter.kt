package com.decard.androidtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decard.androidtest.databinding.LayoutItemBinding
import com.decard.androidtest.db.Trade

class TradeAdapter : ListAdapter<Trade, TradeAdapter.ViewHolder>(TradeDiffCallback()) {

    class ViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Trade) {
            binding.apply {
                trade = item
                executePendingBindings()
            }
        }
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
        val trade = getItem(position)
        holder.bind(trade)
    }
}


private class TradeDiffCallback : DiffUtil.ItemCallback<Trade>() {

    override fun areItemsTheSame(oldItem: Trade, newItem: Trade): Boolean {
        return oldItem.transaction_id == newItem.transaction_id
    }

    override fun areContentsTheSame(oldItem: Trade, newItem: Trade): Boolean {
        return oldItem == newItem
    }
}