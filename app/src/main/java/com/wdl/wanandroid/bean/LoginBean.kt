package com.wdl.wanandroid.bean

import com.wdl.wanandroid.base.BaseResponse

/**
 * Create by: wdl at 2020/4/13 10:09
 */

data class LoginRes(
    val `data`: UserInfo,
    val errorCode: Int,
    val errorMsg: String
)

data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val token: String,
    val type: Int,
    val username: String
)