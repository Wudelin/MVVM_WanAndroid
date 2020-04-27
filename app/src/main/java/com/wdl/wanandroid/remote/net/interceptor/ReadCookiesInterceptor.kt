package com.wdl.wanandroid.remote.net.interceptor

import com.wdl.wanandroid.base.USER_COOKIE
import com.wdl.wanandroid.utils.CacheUtil
import com.wdl.wanandroid.utils.MMKVUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Create by: wdl at 2020/4/23 16:06
 * 获取Cookie
 */
class ReadCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val cookies = CacheUtil.getCookie()
        if (cookies.isNotEmpty()) {
            builder.addHeader("Cookie", cookies)
        }
        return chain.proceed(builder.build())
    }
}