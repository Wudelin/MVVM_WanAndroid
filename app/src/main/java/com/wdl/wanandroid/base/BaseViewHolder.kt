package com.wdl.wanandroid.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by: wdl at 2020/4/15 15:30
 */
open class BaseViewHolder<VB : ViewDataBinding>(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root)
