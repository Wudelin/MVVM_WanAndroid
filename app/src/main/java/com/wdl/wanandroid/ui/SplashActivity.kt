package com.wdl.wanandroid.ui

import android.animation.Animator
import android.os.Bundle
import com.wdl.wanandroid.BaseActivity
import com.wdl.wanandroid.R
import com.wdl.wanandroid.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {

        lav.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                MainActivity.show(this@SplashActivity)
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

}
