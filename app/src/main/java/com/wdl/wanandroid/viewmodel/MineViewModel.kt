package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.repository.MineRepository
import com.wdl.wanandroid.utils.parse
import com.wdl.wanandroid.utils.safeLaunch

/**
 * Create by: wdl at 2020/4/23 15:38
 */
class MineViewModel(private val repository: MineRepository) : ViewModel() {

    val id = MutableLiveData<Int>()
    val rank = MutableLiveData<Int>()
    val count = MutableLiveData<Int>()
    val isRefresh = MutableLiveData(false)

    fun getRankOrRefresh() {
        isRefresh.value = true
        viewModelScope.safeLaunch {
            block = {
                when (val result = repository.getRank().parse()) {
                    is Results.Success -> {
                        id.value = result.data.userId
                        rank.value = result.data.rank
                        count.value = result.data.coinCount
                        isRefresh.value = false
                    }
                    is Results.Failure -> {
                        isRefresh.value = false
                    }
                }
            }
        }
    }
}