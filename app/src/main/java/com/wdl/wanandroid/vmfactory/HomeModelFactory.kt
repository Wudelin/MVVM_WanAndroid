package com.wdl.wanandroid.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.viewmodel.HomeViewModel

/**
 * Create by: wdl at 2020/4/10 17:16
 */
class HomeModelFactory(private val repository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}