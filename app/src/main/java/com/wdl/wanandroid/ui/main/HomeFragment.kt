package com.wdl.wanandroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.R
import com.wdl.wanandroid.adapter.ImgBannerAdapter
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.WEB_URL
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.databinding.FragmentHomeBinding
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.transformer.ScaleInTransformer
import com.wdl.wanandroid.utils.dp2px
import com.wdl.wanandroid.utils.toast
import com.wdl.wanandroid.viewmodel.HomeViewModel
import com.wdl.wanandroid.vmfactory.HomeModelFactory
import com.youth.banner.indicator.CircleIndicator

/**
 * A simple [Fragment] subclass.
 * 首页
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val mBannerAdapter: ImgBannerAdapter by lazy {
        ImgBannerAdapter(requireContext(), homeViewModel.mBannerData.value!!)
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeModelFactory(HomeRepository())).get(HomeViewModel::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        mBinding?.apply {
            mBinding?.model = homeViewModel
            banner.apply {
                adapter = mBannerAdapter
                setDelayTime(5000L)
                setBannerGalleryEffect(20, 30, 0.14f)
                setBannerRound(dp2px(20f))
                indicator = CircleIndicator(requireContext())
                setIndicatorSelectedColorRes(R.color.colorPrimary)
                setOnBannerListener { data, _ ->
                    data as BannerData
                    mNavController.navigate(R.id.action_home_dest_to_webActivity,Bundle().apply {
                        putString(WEB_URL,data.url)
                    })
                }
            }
        }

        homeViewModel.getBannerData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

}
