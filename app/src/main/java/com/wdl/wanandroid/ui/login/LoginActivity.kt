package com.wdl.wanandroid.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.BaseActivity
import com.wdl.wanandroid.R
import com.wdl.wanandroid.databinding.ActivityLoginBinding
import com.wdl.wanandroid.widget.TitleBar

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginModelFactory(LoginRepository())
        ).get(LoginViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.tb.apply {
            setTitle("欢迎登录")
            setRightText("注册")
            mBackListener = mBack
            mRightListener = object : TitleBar.OnRightListener {
                override fun onClick(v: View) {
                }
            }
        }
        mBinding.wtv.setCustomText("忘记密码?")

        mBinding.holder = this

        mBinding.model = loginViewModel

//        loginViewModel.mBtnStatus.observe(this, Observer {
//
//        })
    }

    fun login(view: View) {
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
    }
}
