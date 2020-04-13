package com.wdl.wanandroid.remote.net

import com.wdl.wanandroid.base.BaseResponse
import com.wdl.wanandroid.bean.BannerRes
import com.wdl.wanandroid.bean.LoginRes
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Create by: wdl at 2020/4/9 17:45
 * wan android 接口
 */
interface WanApi {

    /**
     * 登录
     */
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") user: String,
        @Field("password") pwd: String
    ): Response<LoginRes>

    /**
     * 注册
     */
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") user: String,
        @Field("password") pwd: String,
        @Field("repassword") rePwd: String
    ): Response<LoginRes>

    /**
     * 退出登录
     * 清除个人信息-Cookie等
     */
    @GET("/user/logout/json")
    suspend fun logout(): Response<BaseResponse>

    @GET("/banner/json")
    suspend fun banner(): Response<BannerRes>
}