package com.wdl.wanandroid.repository


import com.wdl.wanandroid.remote.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by: wdl at 2020/4/10 17:15
 */
class LoginRepository {

    /**
     * login
     */
    suspend fun login(user: String, pwd: String)
            = withContext(Dispatchers.IO) {
        RetrofitManager.wanService.login(user, pwd)
    }
}