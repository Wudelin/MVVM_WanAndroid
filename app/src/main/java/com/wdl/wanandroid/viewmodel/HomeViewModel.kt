package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.wdl.module_aac.paging.toLiveDataPagedList
import com.wdl.module_aac.paging.PagingRequestHelper
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.utils.WLogger
import com.wdl.wanandroid.utils.parse
import com.wdl.wanandroid.utils.process
import com.wdl.wanandroid.utils.safeLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                when (val result = repository.getBanner().parse()) {
                    is Results.Success -> {
                        WLogger.e(Thread.currentThread().name)
                        mBannerData.value = result.data
                    }
                }
            }
        }
    }

    fun refresh() {
        mIsRefresh.value = true
        viewModelScope.safeLaunch {
            block = {
                when (val result = repository.fetchArticleByPage(0).parse()) {
                    is Results.Success -> {
                        mIsRefresh.value = false
                        repository.cleanAndInsert(result.data.datas)
                    }
                    is Results.Failure -> {
                        mIsRefresh.value = false
                    }
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
                        when (val result = repository.fetchArticleByPage(0).parse()) {
                            is Results.Success -> {
                                handlerRes(result.data.datas, 0)
                                callback.recordSuccess()
                            }
                            is Results.Failure -> {
                                //callback.recordFailure()
                            }
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