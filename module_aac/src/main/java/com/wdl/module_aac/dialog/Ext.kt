package com.wdl.module_aac.dialog

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner

/**
 * Create by: wdl at 2020/4/28 11:39
 */

/**
 * AlertDialog绑定生命周期
 */
fun AlertDialog.lifecycleOwner(owner: LifecycleOwner? = null): AlertDialog {
    val observer = DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}