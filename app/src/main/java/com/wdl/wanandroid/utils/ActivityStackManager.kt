package com.wdl.wanandroid.utils

import android.app.Activity

/**
 * Create by: wdl at 2020/4/9 15:08
 * Activity栈管理类
 */
object ActivityStackManager {
    private val mActivities = mutableListOf<Activity>()

    fun add(activity: Activity) = mActivities.add(activity)

    fun remove(activity: Activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
    }

    fun clear() =
        mActivities.filter { it.isFinishing }.forEach { it.finish() }
}