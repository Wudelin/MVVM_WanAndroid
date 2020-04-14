package com.wdl.wanandroid.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wdl.wanandroid.R
import com.wdl.wanandroid.bean.TabBean
import com.wdl.wanandroid.databinding.TabItemLayoutBinding

/**
 * Create by: wdl at 2020/4/14 11:12
 */
class TabAdapter : BaseQuickAdapter<TabBean, BaseViewHolder>(R.layout.tab_item_layout) {
    lateinit var mBinding: TabItemLayoutBinding
    override fun convert(holder: BaseViewHolder, item: TabBean) {
        mBinding.data = item
        mBinding.executePendingBindings()
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        mBinding = DataBindingUtil.bind(viewHolder.itemView)!!
    }

}