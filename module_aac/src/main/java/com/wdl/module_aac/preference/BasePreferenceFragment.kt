package com.wdl.module_aac.preference

import android.annotation.SuppressLint
import androidx.preference.*
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by: wdl at 2020/4/27 10:58
 * 去除左边ICON的空白
 * 参考：https://blog.csdn.net/csdn0lan/article/details/88760088
 */
abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private fun setAllPreferencesToAvoidHavingExtraSpace(preference: Preference) {
        preference.isIconSpaceReserved = false
        if (preference is PreferenceGroup)
            for (i in 0 until preference.preferenceCount)
                setAllPreferencesToAvoidHavingExtraSpace(preference.getPreference(i))
    }

    override fun setPreferenceScreen(preferenceScreen: PreferenceScreen?) {
        preferenceScreen?.apply {
            setAllPreferencesToAvoidHavingExtraSpace(preferenceScreen)
        }
        super.setPreferenceScreen(preferenceScreen)
    }

    override fun onCreateAdapter(preferenceScreen: PreferenceScreen?): RecyclerView.Adapter<*> =
        object : PreferenceGroupAdapter(preferenceScreen) {
            @SuppressLint("RestrictedApi")
            override fun onPreferenceHierarchyChange(preference: Preference?) {
                if (preference != null)
                    setAllPreferencesToAvoidHavingExtraSpace(preference)
                super.onPreferenceHierarchyChange(preference)
            }
        }
}