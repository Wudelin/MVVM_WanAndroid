package com.wdl.wanandroid.base

import android.content.res.Resources
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.module_aac.navigation.NavHostFragment
import com.wdl.wanandroid.ui.MainFragment
import com.wdl.wanandroid.utils.ActivityStackManager
import com.wdl.wanandroid.utils.BarUtils
import com.wdl.wanandroid.utils.getFragment
import com.wdl.wanandroid.widget.TitleBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.jessyan.autosize.AutoSizeCompat


/**
 * Create by: wdl at 2020/4/9 15:04
 * Activity基类：
 *  统一管理ViewDataBinding的实例化，置为null
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {

    protected var mBack: TitleBar.OnBackListener? = object : TitleBar.OnBackListener {
        override fun onBack(v: View) {
            finish()
        }

    }

    protected var mBinding: VB? = null


    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusBar(true)
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId()) as VB
        // 置灰
        // 参考自：https://juejin.im/post/5e88937951882573c66cf99d
        if (isRequiredGray()) putGray()
        ActivityStackManager.add(this)
        mBinding?.lifecycleOwner = this
        initView(savedInstanceState)
    }

    /** 透明状态栏 */
    open fun transparentStatusBar(lightStatusBar: Boolean) {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
    }

    protected fun isRequiredGray(): Boolean = false

    private fun putGray() {
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.remove(this)
        mBack = null
        mBinding?.unbind()
        mBinding = null
    }

    /**
     * 获取ViewModel
     */
    fun <VM : ViewModel> getViewModel(clazz: Class<VM>): VM = ViewModelProvider(this).get(clazz)

    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

//    override fun onBackPressed() {
//        //判断当前是哪个fragment
//        val fragment = getFragment(MainFragment::class.java)
//        if (fragment != null){
//            finish()
//        }else{
//            super.onBackPressed()
//        }
//    }

}