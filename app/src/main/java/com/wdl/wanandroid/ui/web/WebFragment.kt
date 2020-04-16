package com.wdl.wanandroid.ui.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient

import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.WEB_URL
import com.wdl.wanandroid.databinding.FragmentWebBinding

/**
 * A simple [Fragment] subclass.
 */
class WebFragment : BaseFragment<FragmentWebBinding>() {
    private val mAgentWeb by lazy {
        AgentWeb.with(this)
            .setAgentWebParent(mBinding?.fr!!, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)
    }

    private val url: String? by lazy {
        arguments?.getString(WEB_URL)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mAgentWeb.webCreator.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                mBinding?.tb?.apply {
                    setTitle(title ?: "")
                }
                super.onReceivedTitle(view, title)
            }
        }

        mBinding?.tb?.apply {
            mBackListener = mBack
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_web

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
