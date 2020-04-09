package com.wdl.wanandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wdl.wanandroid.BaseFragment

import com.wdl.wanandroid.R
import com.wdl.wanandroid.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * 首页
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

}
