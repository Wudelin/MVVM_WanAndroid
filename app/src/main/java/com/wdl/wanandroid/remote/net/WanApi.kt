package com.wdl.wanandroid.remote.net

import com.wdl.wanandroid.bean.LoginRes
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Create by: wdl at 2020/4/9 17:45
 * wan android 接口
 */
interface WanApi {

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") user: String,
        @Field("password") pwd: String
    ): Response<LoginRes>

    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") user: String,
        @Field("password") pwd: String,
        @Field("repassword") rePwd: String
    ): Response<LoginRes>
}