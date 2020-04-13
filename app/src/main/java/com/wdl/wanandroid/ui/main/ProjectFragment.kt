package com.wdl.wanandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentProjectBinding

/**
 * A simple [Fragment] subclass.
 * 项目
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding>() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_project

}
