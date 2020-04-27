package com.wdl.wanandroid.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import com.wdl.wanandroid.R
import com.wdl.wanandroid.utils.CacheUtil
import com.wdl.wanandroid.utils.getGlobalViewModel
import com.wdl.wanandroid.utils.getVersion
import com.wdl.wanandroid.viewmodel.GlobalViewModel
import com.wdl.module_aac.preference.BasePreferenceFragment
import com.wdl.wanandroid.viewmodel.MineViewModel
import com.wdl.wanandroid.widget.TitleBar

/**
 * Create by: wdl at 2020/4/27 9:34
 * 系统设置页面
 */
class SettingFragment : BasePreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val appViewModel: GlobalViewModel by lazy { getGlobalViewModel() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 添加Toolbar
        val containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
        containerView.let {
            val linearLayout = it.parent as LinearLayout
            linearLayout.run {
                val toolbar = TitleBar(context)
                toolbar.setTitle("设置")
                toolbar.mBackListener = object : TitleBar.OnBackListener {
                    override fun onBack(v: View) {
                        Navigation.findNavController(view).navigateUp()
                    }
                }
                linearLayout.addView(toolbar, 0)
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_ps, rootKey)

        init()
    }

    /**
     * 缓存中获取设置的初始值
     */
    private fun init() {
        // 是否下载Banner图片
        findPreference<CheckBoxPreference>("download_img")?.isChecked =
            CacheUtil.getAutoDownloadImg()

        findPreference<Preference>("version")?.apply {
            summary = "当前版本:" + requireContext().getVersion()
        }

        findPreference<Preference>("account")?.apply {
            isVisible = CacheUtil.isLogin()
        }
        // 是否登录
        findPreference<Preference>("logout")?.apply {
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                // 退出登录
                appViewModel.userInfo.postValue(null)
                appViewModel.logout.postValue(true)
                CacheUtil.saveUserInfo(null)
                view?.let {
                    Navigation.findNavController(it).navigateUp()
                }
                false
            }
        }

        findPreference<Preference>("clear_cache")?.apply {
            summary = CacheUtil.getTotalCacheSize(requireContext())
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                CacheUtil.clearAllCache(requireActivity())
                summary = "0.00KB"
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * 值改变的回调
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key!!) {
            "download_img" -> CacheUtil.setAutoDownloadImg(
                sharedPreferences!!.getBoolean(
                    key,
                    false
                )
            )
            else -> {

            }
        }
    }
}