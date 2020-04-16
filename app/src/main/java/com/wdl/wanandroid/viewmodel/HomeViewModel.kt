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
import com.wdl.wanandroid.utils.process
import com.wdl.wanandroid.utils.safeLaunch
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    var mBannerData: MutableLiveData<List<BannerData>> =
        MutableLiveData<List<BannerData>>(emptyList())

    /**
     * 首页文章，从DataSource中取
     */
    val mPagedList: LiveData<PagedList<HomeArticleDetail>>
        get() = repository.fetchHomeArticleDF()
            .toLiveDataPagedList(boundaryCallback = mBoundaryCallback)

    val mIsRefresh= MutableLiveData(false)

    fun getBannerData() {
        viewModelScope.safeLaunch {
            block = {
                when (val result = repository.getBanner().process()) {
                    is Results.Success -> {
                        mBannerData.postValue(result.data.data)
                    }
                }
            }
        }
    }

    fun refresh() {
        mIsRefresh.postValue(true)
        viewModelScope.safeLaunch {
            block = {
                when (val result = repository.fetchArticleByPage(0)) {
                    is Results.Success -> {
                        repository.cleanAndInsert(result.data.data.datas)
                    }
                }
                mIsRefresh.postValue(false)
            }
        }
    }

    private val mBoundaryCallback = HomeBoundaryCallback { result, pageIndex ->
        viewModelScope.safeLaunch {
            block = {
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
    }


    inner class HomeBoundaryCallback(
        private val handlerRes: (List<HomeArticleDetail>, Int) -> Unit
    ) : PagedList.BoundaryCallback<HomeArticleDetail>() {

        private val mHelper = PagingRequestHelper(Executors.newSingleThreadExecutor())

        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()

            mHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
                viewModelScope.safeLaunch {
                    block = {
                        when (val result = repository.fetchArticleByPage(0)) {
                            is Results.Success -> {
                                handlerRes(result.data.data.datas, 0)
                            }
                        }
                        it.recordSuccess()
                    }
                }
            }
        }

        override fun onItemAtEndLoaded(itemAtEnd: HomeArticleDetail) {
            super.onItemAtEndLoaded(itemAtEnd)
            viewModelScope.launch {
                val currentPageIndex = ((repository.fetchCount() ?: 0) / 20)
                val nextPageIndex = currentPageIndex + 1
                mHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                    viewModelScope.safeLaunch {
                        block = {
                            when (val result = repository.fetchArticleByPage(nextPageIndex)) {
                                is Results.Success -> {
                                    handlerRes(result.data.data.datas, nextPageIndex)
                                }
                            }
                            it.recordSuccess()
                        }
                    }
                }
            }
        }
    }

}