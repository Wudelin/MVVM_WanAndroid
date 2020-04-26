package com.wdl.wanandroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wdl.wanandroid.R
import com.wdl.wanandroid.adapter.ArticleAdapter
import com.wdl.wanandroid.adapter.ImgBannerAdapter
import com.wdl.wanandroid.adapter.TabAdapter
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.OnItemClickListener
import com.wdl.wanandroid.base.WEB_URL
import com.wdl.wanandroid.bean.BannerData
import com.wdl.wanandroid.bean.TabBean
import com.wdl.wanandroid.databinding.FragmentHomeBinding
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import com.wdl.wanandroid.repository.HomeRepository
import com.wdl.wanandroid.transformer.ScaleInTransformer
import com.wdl.wanandroid.utils.dp2px
import com.wdl.wanandroid.utils.removeAllAnimation
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

    private val mAdapter: ArticleAdapter by lazy { ArticleAdapter() }

    private val mTabAdapter: TabAdapter by lazy {
        TabAdapter().apply {
            data = mutableListOf(
                TabBean("常用网址", R.drawable.ic_net),
                TabBean("导航", R.drawable.ic_nav),
                TabBean("分享文章", R.drawable.ic_share_hone)
            )
        }
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this, HomeModelFactory(
                HomeRepository()
            )
        ).get(HomeViewModel::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        mBinding?.apply {
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                homeViewModel.refresh()
            }
            adapter = mTabAdapter
            model = homeViewModel

            recyclerArticle.apply {
                adapter = mAdapter
                removeAllAnimation()
            }

            banner.apply {
                adapter = mBannerAdapter
                setDelayTime(5000L)
                setBannerRound(dp2px(20f))
                setPageTransformer(
                    ScaleInTransformer()
                )
                indicator = CircleIndicator(requireContext())
                setIndicatorSelectedColorRes(R.color.colorPrimary)
                setOnBannerListener { data, _ ->
                    data as BannerData
                    Navigation.findNavController(view)
                        .navigate(R.id.action_main_fragment_to_webFragment, Bundle().apply {
                            putString(WEB_URL, data.url)
                            //putString(WEB_URL, "https://call.crm.ink/")
                        })
                }
            }
        }

        homeViewModel.getBannerData()

        homeViewModel.mPagedList.observe(this, Observer {
            mAdapter.submitList(it)
            mBinding!!.recyclerArticle.scrollToPosition(0)
        })

        mAdapter.mOnItemClickListener = object : OnItemClickListener<HomeArticleDetail> {

            override fun onItemClick(view: View, position: Int, data: HomeArticleDetail) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_main_fragment_to_webFragment, Bundle().apply {
                        putString(WEB_URL, data.link)
                    })
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

}
