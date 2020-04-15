package com.wdl.wanandroid.repository

import androidx.annotation.WorkerThread
import androidx.paging.DataSource
import androidx.room.withTransaction
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.bean.HomeArticleRes
import com.wdl.wanandroid.db.AppDataBase
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import com.wdl.wanandroid.remote.net.RetrofitManager
import com.wdl.wanandroid.remote.net.WanApi
import com.wdl.wanandroid.utils.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Create by: wdl at 2020/4/10 17:15
 */
class HomeRepository(
    private val localDataSource: HomeLocalDataSource,
    private val remoteDataSource: HomeRemoteDataSource
) {

    /**
     * 获取首页Banner
     */
    suspend fun getBanner() = remoteDataSource.banner()

    /**
     * 本地数据库获取DataSource.Factory
     */
    fun fetchHomeArticleDF(): DataSource.Factory<Int, HomeArticleDetail> =
        localDataSource.fetchHomeArticleFromDB()

    suspend fun fetchArticleByPage(page: Int): Results<HomeArticleRes> =
        remoteDataSource.fetchArticleByPage(page).process()

    suspend fun cleanAndInsert(result: List<HomeArticleDetail>) {
        localDataSource.cleanAndInsert(result)
    }

    suspend fun insertNewPageData(result: List<HomeArticleDetail>) {
        localDataSource.insertNewPageData(result)
    }

    suspend fun fetchCount() = localDataSource.fetchCount()

}

/**
 * 本地数据源
 */
class HomeLocalDataSource(private val db: AppDataBase = AppDataBase.instance) {

    /**
     * 从本地数据库获取文章
     */
    fun fetchHomeArticleFromDB(): DataSource.Factory<Int, HomeArticleDetail> =
        db.getHomeArticleDao().queryAll()

    suspend fun cleanAndInsert(result: List<HomeArticleDetail>) {
        db.withTransaction {
            db.getHomeArticleDao().deleteAll()
            insertInternal(result)
        }
    }

    private suspend fun insertInternal(result: List<HomeArticleDetail>) {
        db.getHomeArticleDao().saveArticles(result)
    }

    suspend fun insertNewPageData(result: List<HomeArticleDetail>) {
        db.withTransaction {
            db.getHomeArticleDao().saveArticles(result)
        }
    }

    suspend fun fetchCount(): Int? {
        return db.withTransaction { db.getHomeArticleDao().fetchCount() }
    }

}

/**
 * 服务端数据源
 */
class HomeRemoteDataSource(private val api: WanApi = RetrofitManager.wanService) {
    /**
     * banner
     */
    @WorkerThread
    suspend fun banner() = withContext(Dispatchers.IO) {
        api.banner()
    }

    /**
     * 网络获取首页文章
     */
    @WorkerThread
    suspend fun fetchArticleByPage(page: Int): Response<HomeArticleRes> =
        withContext(Dispatchers.IO) {
            api.article(page)
        }


}