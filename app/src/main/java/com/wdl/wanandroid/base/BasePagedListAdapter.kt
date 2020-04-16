@file:Suppress("LeakingThis")

package com.wdl.wanandroid.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wdl.wanandroid.utils.WLogger

/**
 * Create by: wdl at 2020/4/16 9:18
 * PagedListAdapter的基类 - 存在刷新时列表闪动问题
 */
abstract class BasePagedListAdapter<T, VB : ViewDataBinding>(val callback: DiffUtil.ItemCallback<T>) :
    PagedListAdapter<T, BaseViewHolder<VB>>(callback) {

    var mOnItemClickListener: OnItemClickListener<T>? = null
    var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> =
        BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutId(viewType),
                parent,
                false
            )
        )

    abstract fun getLayoutId(viewType: Int): Int

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val data = getItemData(position) ?: return
        bindData(data, holder, position)
        holder.binding.executePendingBindings()
        holder.binding.root.setOnClickListener {
            mOnItemClickListener?.onItemClick(it, position, data)
        }
        holder.binding.root.setOnLongClickListener {
            mOnItemLongClickListener?.onItemLongClick(it, position, data)
            false
        }
    }

    abstract fun bindData(data: T, holder: BaseViewHolder<VB>, position: Int)

    open fun getItemData(position: Int): T? = getItem(position)
}


/**
 * 解决列表闪动
 */
abstract class BaseNoBlinkAdapter<T, VB : ViewDataBinding>(cb: DiffUtil.ItemCallback<T>) :
    BasePagedListAdapter<T, VB>(cb) {

    private var mDiffer: AsyncPagedListDiffer<T>? = null

    init {
        mDiffer = AsyncPagedListDiffer(this, callback)
        setHasStableIds(true)
    }

    override fun getItemData(position: Int): T? = mDiffer?.getItem(position)

    override fun getItemCount(): Int = mDiffer?.itemCount ?: 0

    override fun getItemId(position: Int): Long = generateItemId(mDiffer, position)

    /**
     * 生成 id
     */
    abstract fun generateItemId(differ: AsyncPagedListDiffer<T>?, position: Int): Long

    override fun submitList(pagedList: PagedList<T>?) {
        pagedList?.addWeakCallback(pagedList.snapshot(), object : BasePagedListCallback() {
            override fun onInserted(position: Int, count: Int) {
                mDiffer?.submitList(pagedList) {
                    WLogger.e("")
                }
            }
        })
    }

}

abstract class BasePagedListCallback : PagedList.Callback() {
    override fun onChanged(position: Int, count: Int) {

    }

    override fun onInserted(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {

    }
}