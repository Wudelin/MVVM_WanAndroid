package com.wdl.wanandroid

import android.app.Application
import android.content.Context
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.wdl.wanandroid.base.BUG_LY_APP_ID
import com.wdl.wanandroid.base.BUG_LY_APP_KEY
import com.wdl.wanandroid.utils.WLogger
import java.nio.charset.Charset
import java.util.LinkedHashMap

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

        initBugly()
        initX5Tbs()

    }

    /**
     * App 首次就可以加载 x5 内核
     */
    private fun initX5Tbs() {
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                WLogger.e("initX5Environment onCoreInitFinished")
            }

            override fun onViewInitFinished(p0: Boolean) {
                WLogger.e("initX5Environment onViewInitFinished")
            }

        })
    }

    /**
     * tbs X5内核
     */
    private fun initBugly() {
        val strategy = CrashReport.UserStrategy(this)
        strategy.setCrashHandleCallback(object : CrashReport.CrashHandleCallback() {
            override fun onCrashHandleStart(
                crashType: Int,
                errorType: String?,
                errorMessage: String?,
                errorStack: String?
            ): MutableMap<String, String> {
                val map = LinkedHashMap<String, String>()
                val crashInfo =
                    com.tencent.smtt.sdk.WebView.getCrashExtraMessage(applicationContext)
                map["crashInfo"] = crashInfo
                return map
            }

            override fun onCrashHandleStart2GetExtraDatas(
                crashType: Int,
                errorType: String?,
                errorMessage: String?,
                errorStack: String?
            ): ByteArray {
                return try {
                    "Extra data.".toByteArray(Charset.forName("UTF-8"))
                } catch (e: Exception) {
                    ByteArray(0)
                }
            }
        })
        CrashReport.initCrashReport(this, BUG_LY_APP_ID, BuildConfig.DEBUG, strategy)
    }

    companion object {
        lateinit var app: Context
    }
}