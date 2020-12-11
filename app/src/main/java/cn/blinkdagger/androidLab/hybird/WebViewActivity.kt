package cn.blinkdagger.androidLab.hybird

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import cn.blinkdagger.androidLab.R
import cn.blinkdagger.androidLab.base.BaseActivity

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */
class WebViewActivity : BaseActivity(), CommonWebView.LoadListener {
    private var mWebView: CommonWebView? = null
    private var progressBar: ProgressBar? = null
    private var mRootView: FrameLayout? = null
    private var mCustomView: View? = null // 视频播放View
    //原始的url
    private var mUrl: String? = null
    private var mCurrentProgress = 0

    override fun getContentLayout(): Int {
        return R.layout.activity_common_web_view
    }

    override fun initView() {
        mWebView = findViewById(R.id.common_web_view)
        progressBar = findViewById(R.id.common_web_pb)
        mRootView = findViewById(R.id.frame_web_view_root)
        registerForContextMenu(mWebView)
        mWebView?.setBackgroundColor(resources.getColor(R.color.color_f4f4f4))
        mUrl = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        setTitle(title)
    }

    override fun initData() {
        mWebView?.apply {
            setUploadFileParam("image/*", "选择图片", true)
            setListener(this@WebViewActivity, this@WebViewActivity)
            // 如果在没有登录的状态下，则 WebView 不需要缓存
            WebViewSettings.setAllowFileAccess(this.settings, true, mUrl)
            clearCache(true)
            requestFocus()
            loadUrl(mUrl)
        }
    }

    override fun onResume() {
        mWebView?.onResume()
        super.onResume()
    }

    public override fun onPause() {
        mWebView?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (mWebView != null && mWebView!!.canGoBack()) {
            mWebView!!.goBack()
        } else {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onDestroy() {
        mWebView?.onDestroy()
        super.onDestroy()
    }

    override fun useToolbar(): Boolean {
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        mWebView!!.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun onPageFinished(url: String?) {
        mCurrentProgress = progressBar!!.progress
        startProgressAnimation(100, true)
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {}
    override fun onExternalPageRequest(url: String?) {}
    @SuppressLint("ObjectAnimatorBinding")
    private fun startProgressAnimation(newProgress: Int, gone: Boolean) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", mCurrentProgress, newProgress)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (gone) {
                    progressBar!!.progress = 0
                    progressBar!!.visibility = View.GONE
                }
            }
        })
        animator.start()
    }

    /**
     *
     */
    internal inner class CustomWebChromeClient : WebChromeClient() {
        private var isStart = false
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mCurrentProgress = progressBar!!.progress
            startProgressAnimation(newProgress, false)
            if (newProgress == 100) {
                if (!isStart) {
                    isStart = true
                }
            }
        }

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            super.onShowCustomView(view, callback)
            if (mCustomView != null) {
                callback.onCustomViewHidden()
                return
            }
            mCustomView = view
            mRootView?.addView(mCustomView)
            mWebView?.visibility = View.GONE
        }

        override fun onHideCustomView() {
            mWebView?.visibility = View.VISIBLE
            if (mCustomView == null) {
                return
            }
            mRootView?.removeView(mCustomView)
            mCustomView = null
            super.onHideCustomView()
        }
    }
}