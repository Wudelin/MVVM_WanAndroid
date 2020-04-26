package com.wdl.wanandroid.ui.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.OPEN_ALBUM
import com.wdl.wanandroid.databinding.AppPopupLayoutBinding
import com.wdl.wanandroid.databinding.FragmentMineBinding
import com.wdl.wanandroid.repository.MineRepository
import com.wdl.wanandroid.utils.*
import com.wdl.wanandroid.viewmodel.GlobalViewModel
import com.wdl.wanandroid.viewmodel.MineViewModel
import com.wdl.wanandroid.vmfactory.MineModelFactory
import java.io.File


/**
 * 我的页面
 *
 * PopupWindow 参考：
 * https://blog.csdn.net/chenbing81/article/details/52059979
 *
 * icon:
 * https://www.iconfont.cn/collections/detail?spm=a313x.7781069.1998910419.d9df05512&cid=21999
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    private var isFirst = true
    private val mMineViewModel: MineViewModel by lazy {
        ViewModelProvider(this, MineModelFactory(MineRepository())).get(MineViewModel::class.java)
    }

    private var mPopWindow: PopupWindow? = null

    private val mGlobalViewModel: GlobalViewModel by lazy {
        getGlobalViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (CacheUtil.isLogin()) {
            if (isFirst) {
                mMineViewModel.getRankOrRefresh()
                isFirst = !isFirst
            }
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        mBinding?.apply {
            holder = this@MineFragment
            global = mGlobalViewModel
            mine = mMineViewModel
            refreshListener = SwipeRefreshLayout.OnRefreshListener {
                if (CacheUtil.isLogin()) {
                    mMineViewModel.getRankOrRefresh()
                }else{
                    mMineViewModel.isRefresh.value = false
                }
            }
        }
    }

    private fun showPopupWindow() {
        getPop()?.apply {
            showAtLocation(mBinding!!.root, Gravity.BOTTOM, 0, 0)
            darkenBackground(0.5f)
        }
    }

    private fun getPop(): PopupWindow? {
        if (mPopWindow == null) {
            val mBind: AppPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.app_popup_layout,
                null,
                false
            )
            mBind.handler = this
            mPopWindow = PopupWindow(
                mBind.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mPopWindow?.apply {
                isFocusable = true
                setBackgroundDrawable(ColorDrawable(0x7f000000))
                setOnDismissListener {
                    dismissPop()
                }
            }
        }
        return mPopWindow
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_name -> {
                if (!CacheUtil.isLogin()) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_main_fragment_to_loginFragment)
                }
            }
            R.id.ll_mine_rank -> {
            }
            R.id.ll_mine_collect -> {
            }
            R.id.ll_mine_public -> {
            }
            R.id.ll_mine_about_author -> {
            }
            R.id.ll_mine_setting -> {
            }
            R.id.civ -> {
                showPopupWindow()
            }
            else -> return
        }
    }

    fun selectCamera(view: View) {
        Navigation.findNavController(requireActivity(), R.id.fragment_container_view)
            .navigate(R.id.action_main_fragment_to_cameraFragment)
        dismissPop()
    }

    fun selectAlbum(view: View) {
        /**
         * 打开系统相机
         */
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            startActivityForResult(this, OPEN_ALBUM)
        }

        dismissPop()
    }

    fun cancel(view: View) {
        dismissPop()
    }

    private fun dismissPop() {
        getPop()?.apply {
            dismiss()
            darkenBackground(1f)
        }
    }

    private fun darkenBackground(bgColor: Float) = requireActivity().window.dark(bgColor)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_ALBUM) {
                data?.let {
                    requireActivity().toast(data.data.toString())
                }
            }
        }
    }

}
