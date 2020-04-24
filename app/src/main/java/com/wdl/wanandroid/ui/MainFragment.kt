package com.wdl.wanandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentMainBinding
import com.wdl.wanandroid.ui.main.HomeFragment
import com.wdl.wanandroid.ui.main.MineFragment
import com.wdl.wanandroid.ui.main.ProjectFragment

/**
 * A simple [Fragment] subclass.
 * MainFragment
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val mHomeFragment: HomeFragment by lazy {
        HomeFragment()
    }
    private val mProjectFragment: ProjectFragment by lazy {
        ProjectFragment()
    }
    private val mMineFragment: MineFragment by lazy {
        MineFragment()
    }

    val mFragments = arrayListOf<Fragment>()

    init {
        mFragments.apply {
            add(mHomeFragment)
            add(mProjectFragment)
            add(mMineFragment)
        }
    }

    override fun isBackPress(): Boolean = false

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.apply {
            vp2.apply {
                isUserInputEnabled = false // 禁止滑动

                adapter = object : FragmentStateAdapter(this@MainFragment) {
                    override fun getItemCount(): Int = mFragments.size

                    override fun createFragment(position: Int): Fragment = mFragments[position]
                }

                offscreenPageLimit = mFragments.size
            }

            bnv.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home_dest -> vp2.setCurrentItem(0, false)
                    R.id.project_dest -> vp2.setCurrentItem(1, false)
                    R.id.mine_dest -> vp2.setCurrentItem(2, false)
                }
                true
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

}
