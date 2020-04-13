package com.wdl.wanandroid.base

/**
 * Create by: wdl at 2020/4/13 10:16
 * 基础返回类
 */
open class BaseResponse<T>(
    var `data`: T? = null,
    var errorCode: Int = -1,
    var errorMsg: String? = null
)