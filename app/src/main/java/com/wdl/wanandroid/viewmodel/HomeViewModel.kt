package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.work.*
import com.wdl.module_aac.paging.toLiveDataPagedList
import com.wdl.module_aac.paging.PagingRequestHelper
import com.wdl.wanandroid.App
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.utils.WLogger
import com.wdl.wanandroid.utils.parse
import com.wdl.wanandroid.utils.safeLaunch
import com.wdl.wanandroid.worker.DownloadWorker
import com.wdl.wanandroid.worker.SaveWorker
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * Create by: wdl at 2020/4/10 17:03
 *
 * 数据库操作一律不使用witchContext(Dispatchers.IO) => 本身就开启事务
 *
 */
class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    var mBannerData: MutableLiveData<List<BannerData>> =
        MutableLiveData(emptyList())

    /**
     * 首页文章，从DataSource中取
     */
    val mPagedList: LiveData<PagedList<HomeArticleDetail>>
        get() = repository.fetchHomeArticleDF()
            .toLiveDataPagedList(boundaryCallback = mBoundaryCallback)

    val mIsRefresh = MutableLiveData(false)

    fun getBannerData() {
        viewModelScope.safeLaunch {
            block = {
                when (val result = repository.fetchBanner().parse()) {
                    is Results.Success -> {
                        WLogger.e(Thread.currentThread().name)
                        mBannerData.value = result.data
//                        TODO WorkManager 并行下载图片并保存（已完成）
//                        val works = arrayListOf<OneTimeWorkRequest>()
//
//                        result.data.forEach {
//                            works.add(
//                                OneTimeWorkRequestBuilder<DownloadWorker>()
//                                    .setInputData(
//                                        workDataOf("url" to it.imagePath)
//                                    )
//                                    .build()
//                            )
//                        }

//                        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
//                            .setInputData(
//                                workDataOf("url" to result.data[0].imagePath)
//                            )
//                            .build()

//                        val saveWorkRequest = OneTimeWorkRequestBuilder<SaveWorker>()
//                            .setInputMerger(ArrayCreatingInputMerger::class)
//                            .build()
//
//
//                        WorkManager.getInstance(App.app)
//                            .beginWith(works)
//                            .then(saveWorkRequest)
//                            .enqueue()

                    }
                    is Results.Failure -> {

                    }
                }
            }
        }
    }

    fun refresh() {
        mIsRefresh.value = true
        viewModelScope.safeLaunch {
            block = {

                val top = repository.fetchTopArticleByPage().parse()
                val nor = repository.fetchArticleByPage(0).parse()
                mIsRefresh.value = false
                if (top is Results.Success && nor is Results.Success) {
                    repository.cleanAndInsert(top.data.plus(nor.data.datas))
                } else if (top is Results.Success && nor is Results.Failure) {
                    repository.cleanAndInsert(top.data)
                } else if (top is Results.Failure && nor is Results.Success) {
                    repository.cleanAndInsert(nor.data.datas)
                }
            }
        }
    }

    private val mBoundaryCallback = HomeBoundaryCallback { result, pageIndex ->
        viewModelScope.launch {
            // pageIndex = 0 刷新 否则插入
            when (pageIndex == 0) {
                true -> {
                    repository.cleanAndInsert(result)
                }
                false -> {
                    repository.insertNewPageData(result)
                }
            }
        }
    }


    inner class HomeBoundaryCallback(
        private val handlerRes: (List<HomeArticleDetail>, Int) -> Unit
    ) : PagedList.BoundaryCallback<HomeArticleDetail>() {

        private val mHelper = PagingRequestHelper(Executors.newSingleThreadExecutor())

        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()

            mHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
                viewModelScope.safeLaunch {
                    block = {
                        val top = repository.fetchTopArticleByPage().parse()
                        val nor = repository.fetchArticleByPage(0).parse()

                        if (top is Results.Success && nor is Results.Success) {
                            handlerRes(top.data.plus(nor.data.datas), 0)
                            callback.recordSuccess()
                        } else if (top is Results.Success && nor is Results.Failure) {
                            handlerRes(top.data, 0)
                            callback.recordSuccess()
                        } else if (top is Results.Failure && nor is Results.Success) {
                            handlerRes(nor.data.datas, 0)
                            callback.recordSuccess()
                        }
                    }
                }
            }
        }

        override fun onItemAtEndLoaded(itemAtEnd: HomeArticleDetail) {
            super.onItemAtEndLoaded(itemAtEnd)

            mHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                viewModelScope.safeLaunch {
                    block = {
                        val currentPageIndex = ((repository.fetchCount() ?: 0) / 20)
                        val nextPageIndex = currentPageIndex + 1
                        when (val result =
                            repository.fetchArticleByPage(nextPageIndex).parse()) {
                            is Results.Success -> {
                                handlerRes(result.data.datas, nextPageIndex)
                                it.recordSuccess()
                            }
                            is Results.Failure -> {
                                //it.recordFailure()
                            }
                        }
                    }
                }
            }
        }
    }
}