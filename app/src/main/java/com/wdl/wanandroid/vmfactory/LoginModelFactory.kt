package com.wdl.wanandroid.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.viewmodel.LoginViewModel

/**
 * Create by: wdl at 2020/4/10 17:16
 */
class LoginModelFactory(private val repository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}