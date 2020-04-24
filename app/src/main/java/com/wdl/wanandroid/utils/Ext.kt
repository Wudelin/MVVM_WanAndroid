package com.wdl.wanandroid.utils

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.wdl.module_aac.navigation.NavHostFragment
import com.wdl.wanandroid.App
import com.wdl.wanandroid.base.BaseResponse
import com.wdl.wanandroid.base.Errors
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.viewmodel.GlobalViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import java.util.*

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
        } else {
            Results.failure(Errors.NetworkError(responseCode, responseMessage))
        }
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


/**
 * 移除所有差异性计算引发的默认更新动画.
 */
fun RecyclerView.removeAllAnimation() {
    val itemAnimator = DefaultItemNoAnimAnimator()
    this.itemAnimator = itemAnimator
    itemAnimator.supportsChangeAnimations = false
    itemAnimator.addDuration = 0L
    itemAnimator.changeDuration = 0L
    itemAnimator.removeDuration = 0L
}


data class CoroutineCallback(
    var block: suspend () -> Any = {},
    var onError: (Throwable) -> Unit = {}
)

fun CoroutineScope.safeLaunch(
    init: CoroutineCallback.() -> Unit
): Job {
    val callback = CoroutineCallback().apply { this.init() }
    return launch(CoroutineExceptionHandler { _, throwable ->
        callback.onError(throwable)
    } + GlobalScope.coroutineContext) {
        callback.block()
    }
}


fun <T> ViewModel.safeLaunch(block: suspend () -> BaseResponse<T>, result: (Results<T>) -> Unit) {
    viewModelScope.launch {
        runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            result(it.parse())
        }.onFailure {
            // TODO 网络请求失败
            result(Results.failure(Errors.NetworkError(desc = it.message!!)))
        }
    }
}

fun <T> BaseResponse<T>.parse(): Results<T> =
    if (isSuccess()) {
        Results.success(getResponseData())
    } else {
        Results.failure(Errors.NetworkError(getResponseCode(), getResponseMessage()))
    }


@Suppress("UNCHECKED_CAST")
fun <F : Fragment> AppCompatActivity.getFragment(fragmentClass: Class<F>): F? {
    val navHostFragment = this.supportFragmentManager.fragments.first() as NavHostFragment

    navHostFragment.childFragmentManager.fragments.forEach {
        if (fragmentClass.isAssignableFrom(it.javaClass)) {
            return it as F
        }
    }

    return null
}

fun Window.dark(alpha: Float) {
    val attr = attributes
    attr.alpha = alpha
    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    attributes = attr
}

fun AppCompatActivity.getGlobalViewModel() =
    (App.app as App).getAppModelProvider().get(GlobalViewModel::class.java)

fun Fragment.getGlobalViewModel() =
    (App.app as App).getAppModelProvider().get(GlobalViewModel::class.java)


/** Same as [AlertDialog.show] but setting immersive mode in the dialog's window */
fun AlertDialog.showImmersive() {
    // Set the dialog to not focusable
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    // Make sure that the dialog's window is in full screen
    window?.decorView?.systemUiVisibility = FLAGS_FULLSCREEN

    // Show the dialog while still in immersive mode
    show()

    // Set the dialog to focusable again
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}

/** Combination of all flags required to put activity into immersive mode */
const val FLAGS_FULLSCREEN =
    View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION