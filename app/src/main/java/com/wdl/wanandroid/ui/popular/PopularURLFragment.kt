package com.wdl.wanandroid.ui.popular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wdl.wanandroid.R
import com.wdl.wanandroid.adapter.UrlAdapter
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentPopularURLBinding
import com.wdl.wanandroid.repository.PopularRepository
import com.wdl.wanandroid.viewmodel.PopularViewModel
import com.wdl.wanandroid.vmfactory.PopModelFactory

/**
 * A simple [Fragment] subclass.
 * 常用网址
 */
class PopularURLFragment : BaseFragment<FragmentPopularURLBinding>() {

    private val mAdapter: UrlAdapter by lazy {
        UrlAdapter()
    }

    private val mViewModel: PopularViewModel by lazy {
        ViewModelProvider(
            this,
            PopModelFactory(PopularRepository())
        ).get(PopularViewModel::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.apply {
            tb.apply {
                setTitle("热门网站")
                mBackListener = mBack
            }
            recyclerView.apply {
                adapter = mAdapter.apply {
                    // 点击事件
                    setOnItemClickListener { _, view, position ->
                        val data = data[position]
                        // WebFragment
                        Navigation.findNavController(view).navigate(PopularURLFragmentDirections.actionPopularURLFragmentToWebFragment(data.link))
                    }
                }

                layoutManager = FlexboxLayoutManager(context).apply {
                    // 主轴为水平方向，起点在左侧
                    flexDirection = FlexDirection.ROW
                    // 左对齐
                    justifyContent = JustifyContent.FLEX_START
                }

                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
        }
        mViewModel.mUrls.observe(this, Observer {
            mAdapter.setList(it)
        })

        mViewModel.getUrls()

    }

    override fun getLayoutId(): Int = R.layout.fragment_popular_u_r_l

}
