package com.wdl.wanandroid.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wdl.wanandroid.utils.getGlobalViewModel
import com.wdl.wanandroid.viewmodel.GlobalViewModel
import com.wdl.wanandroid.widget.TitleBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Create by: wdl at 2020/4/9 17:16
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {

    protected var mBinding: VB? = null

    val appViewModel: GlobalViewModel by lazy { getGlobalViewModel() }

    protected var mBack: TitleBar.OnBackListener? = object : TitleBar.OnBackListener {
        override fun onBack(v: View) {
            Navigation.findNavController(v).navigateUp()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 非中断保存，不会随着Activity销毁而销毁
        retainInstance = true
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mBinding?.lifecycleOwner = this
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancel()
        mBinding?.unbind()
        mBinding = null
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 第一次创建时初始化-网络请求等
     */
    open fun actionInit() {

    }

    abstract fun getLayoutId(): Int

}