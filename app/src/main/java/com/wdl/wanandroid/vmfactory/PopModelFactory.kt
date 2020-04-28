package com.wdl.wanandroid.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.repository.PopularRepository
import com.wdl.wanandroid.viewmodel.HomeViewModel
import com.wdl.wanandroid.viewmodel.PopularViewModel

/**
 * Create by: wdl at 2020/4/10 17:16
 */
class PopModelFactory(private val repository: PopularRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PopularViewModel(repository) as T
    }
}