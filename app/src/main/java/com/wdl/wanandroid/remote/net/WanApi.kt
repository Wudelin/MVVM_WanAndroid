package com.wdl.wanandroid.remote.net

import com.wdl.wanandroid.base.PagerResponse
import com.wdl.wanandroid.base.WanResponse
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.bean.MineInfo
import com.wdl.wanandroid.bean.UserInfo
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import okhttp3.ResponseBody
import retrofit2.Call
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
    ): Response<WanResponse<UserInfo>>

    /**
     * 注册
     */
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") user: String,
        @Field("password") pwd: String,
        @Field("repassword") rePwd: String
    ): WanResponse<UserInfo>

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

    /**
     * 首页文章置顶列表
     */
    @GET("/article/top/json")
    suspend fun top(): WanResponse<List<HomeArticleDetail>>

    /**
     * 下载图片
     */
    @GET
    @Streaming
    fun downloadImg(@Url imageUrl: String): Call<ResponseBody>


    /**
     * 获取个人积分/排名
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getRank(): WanResponse<MineInfo>


}