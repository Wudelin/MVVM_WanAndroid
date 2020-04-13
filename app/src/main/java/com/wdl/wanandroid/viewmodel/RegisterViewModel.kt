package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.repository.RegisterRepository
import kotlinx.coroutines.launch

/**
 * Create by: wdl at 2020/4/13 14:48
 */
class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {
    var mUser: MutableLiveData<String> = MutableLiveData("")
    var mPwd: MutableLiveData<String> = MutableLiveData("")
    var mRePwd: MutableLiveData<String> = MutableLiveData("")


    fun register(
        username: String,
        password: String,
        rePassword: String,
        success: () -> Unit,
        failure: (String?) -> Unit
    ) {
        if (password == rePassword) {
            viewModelScope.launch {
                when (val result = repository.register(username, password, rePassword)) {
                    is Results.Success -> {
                        if (result.data.errorCode == 0) {
                            success()
                        } else {
                            failure(result.data.errorMsg)
                        }
                    }
                    is Results.Failure -> failure(result.error.message)
                }
            }

        } else {
            failure("两次输入的密码不一致！")
        }
    }

}