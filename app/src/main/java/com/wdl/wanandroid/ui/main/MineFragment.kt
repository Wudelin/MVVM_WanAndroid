package com.wdl.wanandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import com.wdl.wanandroid.BaseFragment

import com.wdl.wanandroid.R
import com.wdl.wanandroid.databinding.FragmentMineBinding
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * A simple [Fragment] subclass.
 * 我的
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        btn_test.setOnClickListener {
            mNavController.navigate(R.id.action_mine_dest_to_loginActivity)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine


}
