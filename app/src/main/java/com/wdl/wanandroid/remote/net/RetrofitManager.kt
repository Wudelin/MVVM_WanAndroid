package com.wdl.wanandroid.remote.net

import com.wdl.wanandroid.base.BASE_URL
import com.wdl.wanandroid.utils.WLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Create by: wdl at 2020/4/9 17:44
 * 网络请求管理类
 */

object RetrofitManager {
    private val TIME_UNIT = TimeUnit.MILLISECONDS
    val wanService: WanApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
            .create(WanApi::class.java)
    }

    private val httpInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            WLogger.e(message)
        }
    })

    private fun getClient(): OkHttpClient {
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .connectTimeout(5_000L, TIME_UNIT)
            .readTimeout(10_000, TIME_UNIT)
            .writeTimeout(30_000, TIME_UNIT)
            .build()
    }
}