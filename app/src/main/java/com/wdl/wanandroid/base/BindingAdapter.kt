package com.wdl.wanandroid.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wdl.wanandroid.adapter.ImgBannerAdapter
import com.wdl.wanandroid.adapter.TabAdapter
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
    banner.setDatas(data)
}

@BindingAdapter(value = ["resId"])
fun convert(view: ImageView, resId: Int) {
    view.setImageResource(resId)
}

@BindingAdapter(value = ["adapter"])
fun setAdapter(view: RecyclerView, adapter: TabAdapter) {
    view.adapter = adapter
}

@BindingAdapter(value = ["onListener", "refreshStatus"])
fun bindRefresh(
    view: SwipeRefreshLayout,
    listener: SwipeRefreshLayout.OnRefreshListener,
    refreshStatus: Boolean
) {
    view.isRefreshing = refreshStatus
    view.setOnRefreshListener(listener)
}

@BindingAdapter(value = [ "refreshEnable"])
fun bindRefreshEnable(
    view: SwipeRefreshLayout,
    refreshStatus: Boolean
) {
    view.isRefreshing = refreshStatus
}