package com.wdl.wanandroid.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.wdl.module_base.widget.WTextView
import com.wdl.wanandroid.R

/**
 * Create by: wdl at 2020/4/10 14:35
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mBack: ImageView
    private var mTitle: WTextView
    private var mRightButton: Button

    var mBackListener: OnBackListener? = null

    var mRightListener: OnRightListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.app_title_view, this, true)
        mBack = findViewById(R.id.iv_back)
        mTitle = findViewById(R.id.wtv_title)
        mRightButton = findViewById(R.id.btn_right)
        mBack.setOnClickListener(this)
        mRightButton.setOnClickListener(this)
    }

    fun setTitle(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        mTitle.setCustomText(text)
    }

    fun setRightText(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        mRightButton.text = text
    }

    fun setTitleVisible(isShow: Boolean) {
        mTitle.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setRightVisible(isShow: Boolean) {
        mRightButton.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    interface OnBackListener {
        fun onBack(v: View)
    }

    interface OnRightListener {
        fun onClick(v: View)
    }

    override fun onClick(v: View?) {
        when (v) {
            mBack -> if (mBackListener != null) mBackListener?.onBack(v)
            mRightButton -> if (mRightListener != null) mRightListener?.onClick(v)
        }
    }


}

