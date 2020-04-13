package com.wdl.wanandroid.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wdl.wanandroid.base.Results
import com.wdl.wanandroid.base.USER_COOKIE
import com.wdl.wanandroid.base.USER_ID
import com.wdl.wanandroid.base.USER_NAME
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.bean.UserInfo
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.utils.MMKVUtil
import com.wdl.wanandroid.utils.process
import kotlinx.coroutines.launch
import okhttp3.Headers

/**
 * Create by: wdl at 2020/4/10 17:03
 */
class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    var mBannerData: MutableLiveData<List<BannerData>> = MutableLiveData<List<BannerData>>(emptyList())

    fun getBannerData() {
        viewModelScope.launch {
            when (val result = repository.banner().process()) {
                is Results.Success -> {
                    mBannerData.postValue(result.data.data)
                }
            }
        }
    }

}