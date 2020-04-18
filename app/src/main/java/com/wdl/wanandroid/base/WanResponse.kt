package com.wdl.wanandroid.base

import java.io.Serializable

/**
 *  基础返回类
 */
class WanResponse<T>(
    private var errorCode: Int,
    private var errorMsg: String,
    private var data: T
) : BaseResponse<T>() {

    /**
     * 判断是否请求成功(WanAndroid 请求码为0时则代表成功)
     */
    override fun isSuccess(): Boolean = errorCode == 0

    override fun getResponseData(): T = data

    override fun getResponseCode(): Int = errorCode

    override fun getResponseMessage(): String = errorMsg
}

/**
 *  分页数据的基类
 */
data class PagerResponse<T>(
    var datas: T,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) : Serializable