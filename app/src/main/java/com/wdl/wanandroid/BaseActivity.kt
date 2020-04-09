package com.wdl.wanandroid

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wdl.wanandroid.utils.ActivityStackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


/**
 * Create by: wdl at 2020/4/9 15:04
 * Activity基类：
 *  统一管理ViewDataBinding的实例化，置为null
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {

    protected val mBinding: VB by lazy {
        DataBindingUtil.setContentView(this, getLayoutId()) as VB
    }

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 置灰
        // 参考自：https://juejin.im/post/5e88937951882573c66cf99d
        if (isRequiredGray()) putGray()

        ActivityStackManager.add(this)
        mBinding.lifecycleOwner = this

        initView(savedInstanceState)
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
        mBinding.unbind()
    }

    /**
     * 获取ViewModel
     */
    fun <VM : ViewModel> getViewModel(clazz: Class<VM>): VM = ViewModelProvider(this).get(clazz)
}