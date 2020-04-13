package com.wdl.wanandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.wdl.wanandroid.R
import com.wdl.wanandroid.adapter.ImgBannerAdapter
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.databinding.FragmentHomeBinding
import com.wdl.wanandroid.repository.HomeRepository
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
        mBinding?.banner?.apply {
            adapter = mBannerAdapter
            setDelayTime(5000L)
            indicator = CircleIndicator(requireContext())
            setIndicatorSelectedColorRes(R.color.colorPrimary)
            setOnBannerListener { data, _ ->
                //TODO 跳转Web
                data as BannerData
                requireContext().toast(data.url)

            }
        }

        homeViewModel.mBannerData.observe(this, Observer {
            mBinding?.banner?.setDatas(it)
        })

        homeViewModel.getBannerData()

    }

    override fun getLayoutId(): Int = R.layout.fragment_home

}
