package cn.blinkdagger.androidLab.hybird

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import cn.blinkdagger.androidLab.R

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */
class WebViewFragment : Fragment(), CommonWebView.LoadListener {
    private var dxyWebView: CommonWebView? = null
    private var progressBar: ProgressBar? = null
    private val mRootView: FrameLayout? = null
    private val mCustomView: View? = null
    private val mCurrentProgress = 0
    private var url: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dxy_web_view, container, false)
        dxyWebView = rootView.findViewById(R.id.dxy_webView)
        progressBar = rootView.findViewById(R.id.dxy_wv_pb)
        dxyWebView?.setListener(this, this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        url = arguments!!.getString(PARAM_ARGS)
        if (TextUtils.isEmpty(url)) {
            return
        }
        initWebView()
    }

    override fun onResume() {
        dxyWebView!!.onResume()
        super.onResume()
    }

    override fun onPause() {
        dxyWebView!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        dxyWebView!!.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        dxyWebView!!.onActivityResult(requestCode, resultCode, intent)
    }

    private fun initWebView() {
        dxyWebView!!.setUploadFileParam("image/*", "选择图片", true)
        dxyWebView!!.setListener(this, this)
        dxyWebView!!.loadUrl(url)
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {}
    override fun onPageFinished(url: String?) {}
    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {}
    override fun onExternalPageRequest(url: String?) {}

    companion object {
        private const val PARAM_ARGS = "url"
        fun newInstance(url: String?): WebViewFragment {
            val args = Bundle()
            args.putString(PARAM_ARGS, url)
            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }
}