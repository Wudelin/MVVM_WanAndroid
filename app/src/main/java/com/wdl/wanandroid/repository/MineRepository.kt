package com.wdl.wanandroid.repository

import com.wdl.wanandroid.remote.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by: wdl at 2020/4/23 15:33
 * 我的
 */
class MineRepository {

    /**
     * 获取排名积分
     */
    suspend fun getRank() = withContext(Dispatchers.IO) {
        RetrofitManager.wanService.getRank()
    }
}