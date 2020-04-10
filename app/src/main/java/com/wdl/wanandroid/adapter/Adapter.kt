package com.wdl.wanandroid.adapter

import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter

/**
 * Create by: wdl at 2020/4/10 18:02
 */
object Adapter {

    @BindingAdapter("user","pwd")
    fun isClick(btn: Button, user: String?, pwd: String?) {
        btn.isEnabled = !(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd))
    }


}