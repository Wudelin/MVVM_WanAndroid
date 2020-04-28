package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.db.bean.PopUrlBean
import com.wdl.wanandroid.repository.PopularRepository
import com.wdl.wanandroid.utils.parse
import com.wdl.wanandroid.utils.safeLaunch

/**
 * Create by: wdl at 2020/4/28 15:09
 */
class PopularViewModel(private val repository: PopularRepository) : ViewModel() {

    val mUrls = MediatorLiveData<List<PopUrlBean>>()

    /**
     * 获取热门网址列表
     * 1.先从数据库读取
     * 2.数据库为空，则网络请求
     * 3.存库
     */
    fun getUrls() {
        // TODO Room查询返回LiveData时，为异步查询，返回的value为空，一种解决方法
        // https://www.soinside.com/question/54ENg4kNPk7HjPNCdDBojb
//        viewModelScope.safeLaunch {
//            block = {
//                val dataForDb = repository.fetchDataFromDb()
//                mUrls.addSource(dataForDb) {
//                    if (it.isNullOrEmpty()) {
//                        viewModelScope.safeLaunch{
//                            block = {
//                                when (val dataForRemote = repository.fetchDataFromRemote().parse()) {
//                                    is Results.Success -> {
//                                        mUrls.value = dataForRemote.data
//                                        repository.saveUrls(dataForRemote.data)
//                                    }
//                                    is Results.Failure -> {
//                                    }
//                                }
//                            }
//                        }
//
//                    } else {
//                        mUrls.removeSource(dataForDb)
//                        mUrls.value = it
//                    }
//                }
//            }

        viewModelScope.safeLaunch {
            block = {
                val dataForDb = repository.fetchDataFromDbRList()
                if (dataForDb.isNullOrEmpty()) {
                    when (val dataForRemote = repository.fetchDataFromRemote().parse()) {
                        is Results.Success -> {
                            mUrls.value = dataForRemote.data
                            repository.saveUrls(dataForRemote.data)
                        }
                        is Results.Failure -> {
                        }
                    }
                } else {
                    mUrls.value = dataForDb
                }
            }
        }

    }
}