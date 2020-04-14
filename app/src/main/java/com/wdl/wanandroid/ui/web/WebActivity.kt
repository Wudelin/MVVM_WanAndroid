package com.wdl.wanandroid.ui.web

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseActivity
import com.wdl.wanandroid.base.WEB_URL
import com.wdl.wanandroid.databinding.ActivityWebviewBinding
import com.wdl.wanandroid.widget.TitleBar
import com.youth.banner.listener.OnBannerListener


class WebActivity : BaseActivity<ActivityWebviewBinding>() {

    private val mAgentWeb by lazy {
        AgentWeb.with(this)
            .setAgentWebParent((mBinding.fr as FrameLayout?)!!, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)
    }

    private val url: String by lazy {
        intent.getStringExtra(WEB_URL)
    }

    override fun getLayoutId(): Int = R.layout.activity_webview

    override fun initView(savedInstanceState: Bundle?) {
        mAgentWeb.webCreator.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                mBinding.tb.apply {
                    setTitle(title ?: "")
                }
                super.onReceivedTitle(view, title)
            }
        }

        mBinding.tb.apply {
            mBackListener = mBack
        }

    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
