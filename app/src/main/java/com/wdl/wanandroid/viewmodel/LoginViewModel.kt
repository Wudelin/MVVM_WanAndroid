package com.wdl.wanandroid.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.base.USER_COOKIE
import com.wdl.wanandroid.base.USER_ID
import com.wdl.wanandroid.base.USER_NAME
import com.wdl.wanandroid.bean.UserInfo
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.utils.MMKVUtil
import com.wdl.wanandroid.utils.process
import kotlinx.coroutines.launch
import okhttp3.Headers

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var mPwd = MutableLiveData("")
    var mUserName = MutableLiveData("")

    fun login(user: String, pwd: String, success: () -> Unit, fail: (String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.login(user, pwd)

            when (val body = result.process()) {
                is Results.Success -> {
                    if (body.data.errorCode == 0) {
                        // 登录成功保存用户相关信息 保存Cookie
                        saveUserInfo(body.data.data)
                        saveHeaders(result.headers())
                        success()
                    } else {
                        fail(body.data.errorMsg)
                    }
                }
                is Results.Failure -> fail(body.error.message)
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

        MMKVUtil.put(USER_COOKIE, cookie)
    }

    private fun saveUserInfo(data: UserInfo) {
        MMKVUtil.put(USER_NAME, data.username)
        MMKVUtil.put(USER_ID, data.id)
    }

}