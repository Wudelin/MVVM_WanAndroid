package com.wdl.wanandroid.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.repository.MineRepository
import com.wdl.wanandroid.viewmodel.MineViewModel

/**
 * Create by: wdl at 2020/4/10 17:16
 */
class MineModelFactory(private val repository: MineRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MineViewModel(repository) as T
    }
}