package com.wdl.wanandroid.repository

import com.wdl.wanandroid.remote.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by: wdl at 2020/4/10 17:15
 */
class RegisterRepository {

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ) = withContext(Dispatchers.IO) {
        RetrofitManager.wanService.register(username, password, rePassword)
    }
}