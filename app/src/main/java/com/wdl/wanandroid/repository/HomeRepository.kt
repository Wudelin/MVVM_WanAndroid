package com.wdl.wanandroid.repository

import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.bean.LoginRes
import com.wdl.wanandroid.remote.net.RetrofitManager
import com.wdl.wanandroid.utils.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by: wdl at 2020/4/10 17:15
 */
class HomeRepository {

    /**
     * banner
     */
    suspend fun banner()
            = withContext(Dispatchers.IO) {
        RetrofitManager.wanService.banner()
    }
}