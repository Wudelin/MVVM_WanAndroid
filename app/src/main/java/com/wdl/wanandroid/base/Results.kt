package com.wdl.wanandroid.base

/**
 * Create by: wdl at 2020/4/13 10:01
 * 公共回调
 */
sealed class Results<out T> {
    companion object {
        fun <T> success(result: T): Results<T> = Success(result)
        fun <T> failure(error: Throwable): Results<T> = Failure(error)
    }

    data class Success<out T>(val data: T) : Results<T>()
    data class Failure(val error: Throwable) : Results<Nothing>()
}