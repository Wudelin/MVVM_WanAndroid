package com.wdl.wanandroid

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import com.wdl.wanandroid.utils.WLogger

/**
 * Create by: wdl at 2020/4/9 17:36
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        app = applicationContext
        WLogger.setIsDebug(BuildConfig.DEBUG)
        val root = MMKV.initialize(this)
        WLogger.e(root)
    }

    companion object {
        lateinit var app: Context
    }
}