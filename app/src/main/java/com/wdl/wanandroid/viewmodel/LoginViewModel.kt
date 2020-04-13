package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.base.USER_NAME
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.utils.MMKVUtil
import kotlinx.coroutines.launch

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var mPwd = MutableLiveData("")
    var mUserName = MutableLiveData("")

    fun login(user: String, pwd: String, success: () -> Unit, fail: (String?) -> Unit) {
        viewModelScope.launch {
            when (val result = repository.login(user, pwd)) {
                is Results.Success -> {
                    if (result.data.errorCode == 0) {
                        // 登录成功保存用户相关信息
                        MMKVUtil.put(USER_NAME, result.data.data.username)
                        success()
                    } else {
                        fail(result.data.errorMsg)
                    }
                }
                is Results.Failure -> fail(result.error.message)
            }
        }
    }

}