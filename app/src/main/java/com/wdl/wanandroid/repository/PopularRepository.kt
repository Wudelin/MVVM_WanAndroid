package com.wdl.wanandroid.repository

import androidx.room.withTransaction
import com.wdl.wanandroid.db.AppDataBase
import com.wdl.wanandroid.db.bean.PopUrlBean
import com.wdl.wanandroid.remote.net.RetrofitManager
import com.wdl.wanandroid.remote.net.WanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by: wdl at 2020/4/28 15:04
 */
class PopularRepository(
    private val localDataSource: LocalDataSource = LocalDataSource(),
    private val remoteDataSource: RemoteDataSource = RemoteDataSource()
) {
    suspend fun fetchDataFromDb(): List<PopUrlBean> = localDataSource.queryAll()
    suspend fun fetchDataFromRemote() = remoteDataSource.fetchData()
    suspend fun saveUrls(data: List<PopUrlBean>) = localDataSource.saveUrls(data)
}


class LocalDataSource(private val db: AppDataBase = AppDataBase.instance) {

    suspend fun queryAll() = withContext(Dispatchers.IO) {
        db.getPopURLDao().queryAll()
    }

    suspend fun saveUrls(urls: List<PopUrlBean>) {
        if (urls.isNullOrEmpty()) return
        db.withTransaction {
            db.getPopURLDao().saveArticles(urls)
        }
    }

}

class RemoteDataSource(private val api: WanApi = RetrofitManager.wanService) {
    suspend fun fetchData() = withContext(Dispatchers.IO) {
        api.getPopularUrl()
    }

}