package com.wdl.wanandroid.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.databinding.ActivityLoginBinding
import com.wdl.wanandroid.repository.LoginRepository
import com.wdl.wanandroid.ui.register.RegisterActivity
import com.wdl.wanandroid.utils.toast
import com.wdl.wanandroid.viewmodel.LoginViewModel
import com.wdl.wanandroid.vmfactory.LoginModelFactory
import com.wdl.wanandroid.widget.TitleBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginModelFactory(LoginRepository())
        ).get(LoginViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.tb.apply {
            setTitle("欢迎登录")
            setRightText("注册")
            mBackListener = mBack
            mRightListener = object : TitleBar.OnRightListener {
                override fun onClick(v: View) {
                    RegisterActivity.start(this@LoginActivity)
                }
            }
        }
        mBinding.wtv.setCustomText("忘记密码?")

        mBinding.holder = this

        mBinding.model = loginViewModel

    }

    /**
     * 登录
     */
    fun login(view: View) {
        val username = et_name.text.toString()
        val password = et_pwd.text.toString()
        if (username.isEmpty() || password.isEmpty()) return
        loginViewModel.login(username, password, {
            finish()
        }, {
            toast(it!!)
        })
    }
}
