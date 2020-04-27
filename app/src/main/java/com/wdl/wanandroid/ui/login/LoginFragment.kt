package com.wdl.wanandroid.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentLoginBinding
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.utils.toast
import com.wdl.wanandroid.viewmodel.LoginViewModel
import com.wdl.wanandroid.vmfactory.LoginModelFactory
import com.wdl.wanandroid.widget.TitleBar

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginModelFactory(LoginRepository())
        ).get(LoginViewModel::class.java)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.apply {
            tb.apply {
                setTitle("欢迎登录")
                setRightText("注册")
                mBackListener = object : TitleBar.OnBackListener {
                    override fun onBack(v: View) {
                        Navigation.findNavController(v).navigateUp()
                    }

                }
                mRightListener = object : TitleBar.OnRightListener {
                    override fun onClick(v: View) {
                        Navigation.findNavController(v)
                            .navigate(R.id.action_loginFragment_to_registerFragment)
                    }
                }
            }

            wtv.setCustomText("忘记密码?")

            holder = this@LoginFragment

            model = loginViewModel
        }


    }

    override fun getLayoutId(): Int = R.layout.fragment_login

    /**
     * 登录
     */
    fun login(view: View) {
        val username = mBinding?.etName?.text.toString()
        val password = mBinding?.etPwd?.text.toString()
        if (username.isEmpty() || password.isEmpty()) return
        loginViewModel.login(username, password, {
            appViewModel.userInfo.value = it
            appViewModel.logout.value = false
            // 登录成功
            Navigation.findNavController(view)
                .navigateUp()
        }, {
            requireContext().toast(it!!)
        })
    }

}
