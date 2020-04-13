package com.wdl.wanandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.USER_NAME
import com.wdl.wanandroid.databinding.FragmentMineBinding
import com.wdl.wanandroid.utils.MMKVUtil
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * A simple [Fragment] subclass.
 * 我的
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        btn_test.setOnClickListener {
            if (MMKVUtil.get(USER_NAME, "").toString().isEmpty())
                mNavController.navigate(R.id.action_mine_dest_to_loginActivity)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine


}
