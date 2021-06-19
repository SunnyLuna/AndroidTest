package com.decard.uilibs.md

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decard.uilibs.R
import com.example.commonlibs.utils.ResourcesUtils
import kotlinx.android.synthetic.main.layout_item.view.*
import kotlinx.android.synthetic.main.layout_refresh_footer.view.*


public class MaterialAdapter(var context: Context, var list: ArrayList<DataBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 普通布局
    private val TYPE_ITEM = 1

    // 脚布局
    private val TYPE_FOOTER = 2

    // 当前加载状态，默认为加载完成
    private var loadState = 2

    // 正在加载
    val LOADING = 1

    // 加载完成
    val LOADING_COMPLETE = 2

    // 加载到底
    val LOADING_END = 3

    private var mItemClickListener: ClickListener? = null

    fun addDatas(dataBeans: ArrayList<DataBean>) {
        list.addAll(0, dataBeans)
        notifyDataSetChanged()
    }

    public fun setOnItemClickListener(clickListener: ClickListener) {
        mItemClickListener = clickListener
    }

    public interface ClickListener {
        fun onItemClick(position: Int)
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        fun getItemDetails(): ItemDetails<Long>? {
            return object : ItemDetails<Long>() {
                override fun getPosition(): Int {
                    return adapterPosition
                }

                @Nullable
                override fun getSelectionKey(): Long? {
                    return itemId
                }
            }
        }

        fun bind(isActivated: Boolean = false) {
            if (isActivated) {
                itemView.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimaryDark))
            } else {
                itemView.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimary))
            }
        }
    }

    class MyFootViewHolder(footView: View) : RecyclerView.ViewHolder(footView) {

    }


    override fun getItemViewType(position: Int): Int {
        // 最后一个item设置为FooterView
        return if (position + 1 == itemCount) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
            )
        } else {
            MyFootViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_refresh_footer, parent, false)
            )
        }

    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            Glide.with(context).load(list[position].img).into(holder.itemView.iv_item_photo)
            holder.itemView.tv_card.text = list[position].title
            holder.itemView.setOnClickListener {
                mItemClickListener!!.onItemClick(position)
            }
        } else if (holder is MyFootViewHolder) {
            val footViewHolder = holder as MyFootViewHolder
            when (loadState) {
                LOADING -> {
                    footViewHolder.itemView.pb_loading.visibility = View.VISIBLE
                    footViewHolder.itemView.tv_loading.visibility = View.VISIBLE
                    footViewHolder.itemView.ll_end.visibility = View.GONE
                }
                LOADING_COMPLETE -> {
                    footViewHolder.itemView.pb_loading.visibility = View.INVISIBLE
                    footViewHolder.itemView.tv_loading.visibility = View.INVISIBLE
                    footViewHolder.itemView.ll_end.visibility = View.GONE
                }
                LOADING_END -> {
                    footViewHolder.itemView.pb_loading.visibility = View.GONE
                    footViewHolder.itemView.tv_loading.visibility = View.GONE
                    footViewHolder.itemView.ll_end.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }

    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    fun setLoadState(loadState: Int) {
        this.loadState = loadState
        notifyDataSetChanged()
    }


    fun setSelectionTracker(mSelectionTracker: SelectionTracker<Long>) {
        tracker = mSelectionTracker
    }
}