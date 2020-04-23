package com.wdl.wanandroid.remote.net.interceptor

import android.text.TextUtils
import com.wdl.wanandroid.base.USER_COOKIE
import com.wdl.wanandroid.utils.CacheUtil
import com.wdl.wanandroid.utils.MMKVUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Create by: wdl at 2020/4/23 16:07
 * 保存Cookie
 */
class SaveCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val responseOrigin: Response = chain.proceed(chain.request())
//
//        val cookies = StringBuilder()
//
//        if (!CacheUtil.isLogin()) {
//
//            responseOrigin.headers.filter { TextUtils.equals(it.first, "Set-Cookie") }
//                .forEach { cookies.append(it.second).append(";") }
//
//            val cookie =
//                if (cookies.endsWith(";")) {
//                    cookies.substring(0, cookies.length - 1)
//                } else {
//                    cookies.toString()
//                }
//
//            MMKVUtil.put(USER_COOKIE, cookie)
//        }
        return responseOrigin
    }

}