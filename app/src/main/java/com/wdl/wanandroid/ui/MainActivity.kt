package com.wdl.wanandroid.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.tencent.bugly.crashreport.CrashReport
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        fun show(context: Context) =
            context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

//        CrashReport.testJavaCrash()
//        val host: NavHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment?
//                ?: return

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
}
