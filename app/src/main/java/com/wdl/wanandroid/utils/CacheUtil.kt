package com.wdl.wanandroid.utils

import com.google.gson.Gson
import com.wdl.wanandroid.bean.UserInfo

/**
 * Create by: wdl at 2020/4/23 10:31
 */
object CacheUtil {

    /**
     * 获取用户信息
     */
    fun getUserInfo(): UserInfo? {
        val userInfo: String = MMKVUtil.get("userInfo", "") as String
        return if (userInfo.isEmpty()) {
            null
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

}