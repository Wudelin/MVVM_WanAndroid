package com.wdl.wanandroid.ui.web

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tencent.smtt.sdk.WebView
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.base.WEB_URL
import com.wdl.wanandroid.databinding.FragmentWebBinding
import com.wdl.module_base.x5web.X5WebView


/**
 * A simple [Fragment] subclass.
 */
class WebFragment : BaseFragment<FragmentWebBinding>() {

    private val mWebView: X5WebView by lazy {
        X5WebView(requireContext(), null)
    }

    private val url: String? by lazy {
        arguments?.getString(WEB_URL)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        requireActivity().window.setFormat(PixelFormat.TRANSLUCENT);

        mBinding?.apply {
            fr.addView(
                mWebView, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            mWebView.webChromeClient = object : com.tencent.smtt.sdk.WebChromeClient() {
                override fun onReceivedTitle(p0: WebView?, title: String?) {

                    tb.apply {
                        setTitle(title ?: "")
                    }
                    super.onReceivedTitle(p0, title)
                }

                override fun onProgressChanged(p0: WebView?, p1: Int) {
                    super.onProgressChanged(p0, p1)
                    if (p1 == 100) {
                        pb.visibility = View.GONE
                    } else {
                        pb.visibility = View.VISIBLE
                        pb.progress = p1
                    }

                }

            }
            mWebView.loadUrl(url)

            tb.apply {
                mBackListener = mBack
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_web


    override fun onDestroyView() {
        super.onDestroyView()
        mWebView.destroy()
    }
}
