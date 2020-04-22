package com.wdl.wanandroid.ui.main

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.AppPopupLayoutBinding
import com.wdl.wanandroid.databinding.FragmentMineBinding
import com.wdl.wanandroid.utils.dark
import com.wdl.wanandroid.utils.toast


/**
 * 我的页面
 *
 * PopupWindow 参考：
 * https://blog.csdn.net/chenbing81/article/details/52059979
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    private var mPopWindow: PopupWindow? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
//        btn_test.setOnClickListener {
//            if (MMKVUtil.get(USER_NAME, "").toString().isEmpty())
//                Navigation.findNavController(it).navigate(R.id.action_main_fragment_to_loginFragment)
//        }

        mBinding?.apply {
            civ.setOnClickListener {
                showPopupWindow()
            }
        }
    }

    private fun showPopupWindow() {
        getPop()?.apply {
            if (!isShowing) {
                showAtLocation(mBinding!!.root, Gravity.BOTTOM, 0, 0)
                darkenBackground(0.5f)
            }
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
            }
        }
        return mPopWindow
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    fun selectCamera(view: View) {
        // TODO 拍照选取
        requireActivity().toast("selectCamera")
        dismiss()
    }

    fun selectAlbum(view: View) {
        // TODO 相册选取
        requireActivity().toast("selectAlbum")
        dismiss()
    }

    fun cancel(view: View) {
        // TODO 取消
        dismiss()
    }

    private fun dismiss() {
        getPop()?.apply {
            if (isShowing) {
                dismiss()
                darkenBackground(1f)
            }
        }
    }

    private fun darkenBackground(bgColor: Float) = requireActivity().window.dark(bgColor)

}
