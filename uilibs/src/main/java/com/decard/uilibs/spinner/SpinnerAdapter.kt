package com.decard.uilibs.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.decard.uilibs.R
import com.decard.uilibs.databinding.LayoutSpinnerItemBinding

public class SpinnerAdapter<T>(private var listData: List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "---SpinnerAdapter"
    var selectedPosition = 0
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return SpinnerViewHolder<T>(LayoutSpinnerItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SpinnerViewHolder<T>).spinnerText.set(listData[position].toString())
        if (selectedPosition == position) {
            holder.spinnerTextColor.set(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.spinnerTextColor.set(mContext.resources.getColor(R.color.colorPrimary))
        }
    }

    inner class SpinnerViewHolder<T>(private var spinnerBinding: LayoutSpinnerItemBinding) :
        RecyclerView.ViewHolder(spinnerBinding.root) {
        public var spinnerText: ObservableField<String> = ObservableField()
        public var spinnerTextColor: ObservableField<Int> = ObservableField()

        init {
            spinnerBinding.spinner = this
            spinnerBinding.setClickListener {
                mClickListener!!.onItemClick(spinnerText.get()!!)
                if (selectedPosition != adapterPosition) {
                    spinnerTextColor.set(mContext.resources.getColor(R.color.colorAccent))
                    selectedPosition = adapterPosition

                }
            }
        }
    }

    public var mClickListener: OnClickListener? = null

    public interface OnClickListener {
        fun onItemClick(msg: String)
    }

    fun setOnItemClickListener(onItemClickListener: OnClickListener?) {
        mClickListener = onItemClickListener
    }
}