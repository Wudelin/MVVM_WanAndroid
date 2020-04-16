package com.wdl.wanandroid.base

import android.view.View

/**
 * Create by: wdl at 2020/4/16 10:46
 * 长按
 */
interface OnItemLongClickListener<T> {
    fun onItemLongClick(view: View, position: Int, data: T)
}