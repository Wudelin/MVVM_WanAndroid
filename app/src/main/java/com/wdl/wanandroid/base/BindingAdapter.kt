package com.wdl.wanandroid.base

import androidx.databinding.BindingAdapter
import com.wdl.wanandroid.adapter.ImgBannerAdapter
import com.wdl.wanandroid.bean.BannerData
import com.youth.banner.Banner

/**
 * Create by: wdl at 2020/4/13 16:57
 */


@BindingAdapter(value = ["data"])
fun setBannerData(
    banner: Banner<BannerData, ImgBannerAdapter>,
    data: List<BannerData>
) {
    data.let { banner.setDatas(it) }
}