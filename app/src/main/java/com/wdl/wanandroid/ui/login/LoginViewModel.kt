package com.wdl.wanandroid.ui.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class LoginViewModel(repository: LoginRepository) : ViewModel() {

    var mPwd = MutableLiveData<String>("")
    var mUserName = MutableLiveData<String>("")
    var mBtnStatus = MutableLiveData<Boolean>(false)

    fun login(user: String?, pwd: String?) {

    }

}