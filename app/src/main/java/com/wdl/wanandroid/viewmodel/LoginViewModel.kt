package com.wdl.wanandroid.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Errors
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.bean.UserInfo
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.utils.CacheUtil
import com.wdl.wanandroid.utils.parse
import com.wdl.wanandroid.utils.process
import com.wdl.wanandroid.utils.safeLaunch
import okhttp3.Headers

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var mPwd = MutableLiveData("")
    var mUserName = MutableLiveData("")

    fun login(user: String, pwd: String, success: (UserInfo) -> Unit, fail: (String?) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = repository.login(user, pwd)
                when (result.process()) {
                    is Results.Success -> {
                        when (val data = result.body()!!.parse()) {
                            is Results.Success -> {
                                // 登录成功保存用户相关信息 保存Cookie
                                saveUserInfo(data.data)
                                saveHeaders(result.headers())
                                success(data.data)
                            }
                            is Results.Failure -> fail((data.error as Errors.NetworkError).desc)
                        }

                    }
                    is Results.Failure -> fail(result.message())
                }
            }
        }
    }

    /**
     * 保存Cookie
     */
    private fun saveHeaders(headers: Headers) {
        val cookies = StringBuilder()

        headers.filter { TextUtils.equals(it.first, "Set-Cookie") }
            .forEach { cookies.append(it.second).append(";") }

        val cookie =
            if (cookies.endsWith(";")) {
                cookies.substring(0, cookies.length - 1)
            } else {
                cookies.toString()
            }

        CacheUtil.saveCookie(cookie)
    }

    private fun saveUserInfo(data: UserInfo) = CacheUtil.saveUserInfo(data)

}