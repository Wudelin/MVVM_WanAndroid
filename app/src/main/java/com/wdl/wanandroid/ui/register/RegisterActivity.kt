package com.wdl.wanandroid.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.databinding.ActivityRegisterBinding
import com.wdl.wanandroid.repository.RegisterRepository
import com.wdl.wanandroid.ui.login.LoginActivity
import com.wdl.wanandroid.utils.toast
import com.wdl.wanandroid.viewmodel.RegisterViewModel
import com.wdl.wanandroid.vmfactory.RegisterModelFactory
import com.wdl.wanandroid.widget.TitleBar

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_register

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            RegisterModelFactory(RegisterRepository())
        ).get(RegisterViewModel::class.java)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.tb.apply {
            setRightText("登录")
            setTitle("欢迎注册")
            mBackListener = mBack
            mRightListener = object : TitleBar.OnRightListener {
                override fun onClick(v: View) {
                    LoginActivity.start(this@RegisterActivity)
                }
            }
        }

        mBinding.holder = this
        mBinding.model = registerViewModel
    }

    fun register() {
        registerViewModel.register(mBinding.etName.text.toString(), mBinding.etPwd.text.toString(),
            mBinding.etRePwd.text.toString(), {
                // 注册成功
                finish()
            }, {
                toast(it!!)
            })
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, RegisterActivity::class.java))
    }
}
