package com.wdl.wanandroid.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wdl.module_aac.navigation.NavHostFragment
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.databinding.ActivityMainBinding
import com.wdl.wanandroid.utils.toast
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var pressTime: Long = 0L
    val times: Long = 2000L

    val host: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
    }


    companion object {
        fun show(context: Context) =
            context.startActivity(Intent(context, MainActivity::class.java))

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

//        CrashReport.testJavaCrash()

//        // 获取控制器
//        val navController = host.navController
//        setupBottomNavMenu(navController)
//
//        /**
//         * 可省略
//         */
//        navController.addOnDestinationChangedListener { _, _, _ ->
//
//        }

    }

    /**
     * 将BottomNav点击事件转交给NavController
     * 注意：menu文件中对应项的ID必须和nav文件中的id一致
     */
    private fun setupBottomNavMenu(navController: NavController) {
        // bnv.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.wan_nav).navigateUp()
    }

    override fun onBackPressed() {
        if (pressTime + times > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            applicationContext.toast("再按一次退出APP~~")
            pressTime = System.currentTimeMillis()
        }
    }
}
