package com.wdl.wanandroid.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentRegisterBinding
import com.wdl.wanandroid.repository.RegisterRepository
import com.wdl.wanandroid.utils.toast
import com.wdl.wanandroid.viewmodel.RegisterViewModel
import com.wdl.wanandroid.vmfactory.RegisterModelFactory
import com.wdl.wanandroid.widget.TitleBar

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            RegisterModelFactory(RegisterRepository())
        ).get(RegisterViewModel::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.apply {
            tb.apply {
                setTitle("欢迎注册")
                mBackListener = object : TitleBar.OnBackListener {
                    override fun onBack(v: View) {
                        Navigation.findNavController(v).navigateUp()
                    }

                }
            }

            holder = this@RegisterFragment
            model = registerViewModel
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_register

    fun register() {
        registerViewModel.register(mBinding?.etName?.text.toString(),
            mBinding?.etPwd?.text.toString(),
            mBinding?.etRePwd?.text.toString(),
            {
                // 注册成功
                Navigation.findNavController(mBinding!!.btnRegister).navigateUp()
            },
            {
                requireContext().toast(it!!)
            })
    }
}
