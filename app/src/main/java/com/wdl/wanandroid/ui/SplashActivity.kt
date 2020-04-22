package com.wdl.wanandroid.ui

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.databinding.ActivitySplashBinding

/**
 * 引导页 -> 进行权限处理
 */

private const val PERMISSIONS_REQUEST_CODE = 0x01
private val PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.apply {
            lav.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 进行权限判断 授予后跳转MainFragment
                        if (!hasPermissions(this@SplashActivity)) {
                            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
                        } else {
                            MainActivity.show(this@SplashActivity)
                            finish()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // Take the user to the success fragment when permission is granted
                MainActivity.show(this)
            }
            finish()
        }
    }

    companion object {
        /**
         * 判断是否有权限
         */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
