package com.wdl.wanandroid.base

import java.io.Serializable

/**
 * Create by: wdl at 2020/4/13 10:16
 * 基础返回类
 */
abstract class BaseResponse<T> : Serializable {
    abstract fun isSuccess(): Boolean
    abstract fun getResponseData(): T
    abstract fun getResponseCode(): Int
    abstract fun getResponseMessage(): String
}