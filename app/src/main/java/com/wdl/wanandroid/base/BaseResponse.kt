package com.wdl.wanandroid.base

/**
 * Create by: wdl at 2020/4/13 10:16
 * 基础返回类
 */
data class BaseResponse(
    var `data`: Any?,
    var errorCode: Int,
    var errorMsg: String?
)