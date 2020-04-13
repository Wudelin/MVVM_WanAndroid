package com.wdl.wanandroid.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.repository.RegisterRepository
import com.wdl.wanandroid.viewmodel.RegisterViewModel

/**
 * Create by: wdl at 2020/4/10 17:16
 */
class RegisterModelFactory(private val repository: RegisterRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(repository) as T
    }
}