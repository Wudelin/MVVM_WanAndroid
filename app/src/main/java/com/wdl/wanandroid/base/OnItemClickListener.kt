package com.wdl.wanandroid.base

import android.view.View

/**
 * Create by: wdl at 2020/4/16 10:46
 * 点击事件
 */
interface OnItemClickListener<T> {
    fun onItemClick(view: View, position: Int, data: T)
}