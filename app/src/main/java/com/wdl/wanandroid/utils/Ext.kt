package com.wdl.wanandroid.utils

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.util.TypedValue
import android.widget.Toast
import com.wdl.wanandroid.base.Results
import retrofit2.Response
import java.io.IOException

/**
 * Create by: wdl at 2020/4/13 11:30
 */

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun Context.toast(resId: Int) =
    Toast.makeText(this, getText(resId), Toast.LENGTH_SHORT).show()


/**
 * Create by: wdl at 2020/4/13 10:37
 * Response转 全局公共的 Results
 */
fun <T> Response<T>.process(): Results<T> {
    return try {
        val responseCode = code()
        val responseMessage = message()
        if (isSuccessful) {
            Results.success(body()!!)
        } else
            Results.failure(Errors.NetworkError(responseCode, responseMessage))
    } catch (e: IOException) {
        Results.failure(Errors.NetworkError())
    }
}


fun String.isEmpty(): Boolean {
    return TextUtils.isEmpty(this)
}

fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    )
}