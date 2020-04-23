package com.wdl.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.wdl.module_aac.livedata.UnPeekLiveData
import com.wdl.wanandroid.bean.UserInfo
import com.wdl.wanandroid.utils.CacheUtil

/**
 * Create by: wdl at 2020/4/23 10:25
 * 全局ViewModel：可管理主题色/用户信息等
 */
class GlobalViewModel : ViewModel() {

    val userInfo: UnPeekLiveData<UserInfo> =
        UnPeekLiveData()

    init {
        userInfo.value = CacheUtil.getUserInfo()
    }
}