package com.wdl.wanandroid.utils

import android.content.Context
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.wdl.wanandroid.base.USER_COOKIE
import com.wdl.wanandroid.bean.UserInfo
import java.io.File
import java.math.BigDecimal

/**
 * Create by: wdl at 2020/4/23 10:31
 */
object CacheUtil {

    fun saveCookie(cookie: String) = MMKVUtil.put(USER_COOKIE, cookie)

    fun getCookie(): String = MMKVUtil.get(USER_COOKIE, "") as String

    /**
     * 获取用户信息
     */
    fun getUserInfo(): UserInfo? {
        val userInfo: String = MMKVUtil.get("userInfo", "") as String
        return if (userInfo.isEmpty()) {
            UserInfo()
        } else {
            Gson().fromJson(userInfo, UserInfo::class.java)
        }
    }

    /**
     * 保存用户信息
     */
    fun saveUserInfo(userInfo: UserInfo?) {
        if (userInfo == null) {
            MMKVUtil.put("userInfo", "")
        } else {
            MMKVUtil.put("userInfo", Gson().toJson(userInfo))
        }
    }

    /**
     * 判断是否登陆
     */
    fun isLogin(): Boolean = (MMKVUtil.get("userInfo", "") as String).isNotEmpty()

    /**
     * 是否自动下载Banner图片
     */
    fun getAutoDownloadImg(): Boolean = (MMKVUtil.get("auto_download", false) as Boolean)

    fun setAutoDownloadImg(isAuto: Boolean) = MMKVUtil.put("auto_download", isAuto)

    /**
     * 获取缓存长度
     */
    fun getFolderSize(file: File?): Long {
        var size = 0L
        file?.run {
            try {
                this.listFiles()!!.forEach {
                    size += if (it.isDirectory) {
                        getFolderSize(it)
                    } else {
                        it.length()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return size
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {

        val kb = size / 1024
        if (kb < 1) {
            return size.toString() + "Byte"
        }

        val mb = kb / 1024

        if (mb < 1) {
            val result1 = BigDecimal(kb.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gb = mb / 1024

        if (gb < 1) {
            val result2 = BigDecimal(mb.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val tb = gb / 1024

        if (tb < 1) {
            val result3 = BigDecimal(gb.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(tb)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            children!!.indices.forEach { i ->
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

    fun getTotalCacheSize(context: Context): String {

        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    fun clearAllCache(activity: FragmentActivity?) {
        activity?.let {
            deleteDir(it.cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                if (it.externalCacheDir == null) {
                    it.toast("清理缓存失败")
                }
                return
            }
            it.externalCacheDir?.let { file ->
                if (deleteDir(file)) {
                    activity.toast("清理缓存成功")
                }
            }
        }
    }

}