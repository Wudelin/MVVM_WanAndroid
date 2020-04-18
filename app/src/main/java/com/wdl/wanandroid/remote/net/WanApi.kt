package com.wdl.wanandroid.remote.net

import com.wdl.wanandroid.base.BaseResponse
import com.wdl.wanandroid.base.PagerResponse
import com.wdl.wanandroid.base.WanResponse
import com.wdl.wanandroid.bean.ArticleData
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.bean.HomeArticleRes
import com.wdl.wanandroid.bean.LoginRes
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import retrofit2.Response
import retrofit2.http.*

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
    suspend fun logout()

    /**
     * 首页Banner数据
     */
    @GET("/banner/json")
    suspend fun banner(): WanResponse<List<BannerData>>

    /**
     * 首页文章列表
     */
    @GET("/article/list/{page}/json")
    suspend fun article(@Path("page") page: Int = 0): WanResponse<PagerResponse<List<HomeArticleDetail>>>
}