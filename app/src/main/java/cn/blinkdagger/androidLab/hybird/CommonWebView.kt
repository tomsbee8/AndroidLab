package cn.blinkdagger.androidLab.hybird

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.webkit.WebStorage.QuotaUpdater
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.blinkdagger.androidLab.BuildConfig
import cn.blinkdagger.androidLab.R
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description 通用webView
 * @Version
 */
class CommonWebView : WebView {
    /**
     * 页面加载相关监听
     */
    interface LoadListener {
        // 页面开始加载
        fun onPageStarted(url: String?, favicon: Bitmap?)

        // 页面加载结束
        fun onPageFinished(url: String?)

        // 页面加载出错
        fun onPageError(errorCode: Int, description: String?, failingUrl: String?)

        // 无法处理非法的url
        fun onExternalPageRequest(url: String?)
    }

    protected var mActivity: WeakReference<Activity?>? = null
    protected var mFragment: WeakReference<Fragment?>? = null
    private var mListener: LoadListener? = null
    /**
     * 文件上传回调 （before Android 5.0 ）
     */
    protected var mFileUploadCallbackFirst: ValueCallback<Uri?>? = null
    /**
     * 文件上传回调 （after Android 5.0 +）
     */
    protected var mFileUploadCallbackSecond: ValueCallback<Array<Uri>>? = null
    protected var mCustomWebViewClient: WebViewClient? = null
    protected var mCustomWebChromeClient: WebChromeClient? = null
    protected var mGeolocationEnabled = false // 允许定位
    // 文件选择的RequestCode
    protected var mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER
    // 选择上传本地文件的类型
    protected var mUploadableFileTypes = "*/*"
    // 选择上传本地文件的时候的标题
    protected var mUploadableFileTitle: String? = "选择文件"
    // 选择上传本地文件的时候拍摄照片
    protected var mCapturePictureEnable = false
    // 拍摄照片的路径
    private var mCameraPhotoPath: String? = null
    // 根据这两个变量配合shouldOverrideUrlLoading监听重定向的时候页面加载开始和加载完成
    private var isLoadingFinished = true
    private var isRedirect = false


    companion object {
        const val REQUEST_CODE_FILE_PICKER = 10086
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    fun setListener(activity: Activity?, listener: LoadListener?) {
        setListener(activity, listener, REQUEST_CODE_FILE_PICKER)
    }

    fun setListener(activity: Activity?, listener: LoadListener?, requestCodeFilePicker: Int) {
        mActivity = activity?.let { WeakReference(it) }
        setListener(listener, requestCodeFilePicker)
    }

    fun setListener(fragment: Fragment?, listener: LoadListener?) {
        setListener(fragment, listener, REQUEST_CODE_FILE_PICKER)
    }

    fun setListener(fragment: Fragment?, listener: LoadListener?, requestCodeFilePicker: Int) {
        mFragment = fragment?.let { WeakReference(it) }
        setListener(listener, requestCodeFilePicker)
    }

    protected fun setListener(listener: LoadListener?, requestCodeFilePicker: Int) {
        mListener = listener
        mRequestCodeFilePicker = requestCodeFilePicker
    }

    fun setUploadFileParam(mUploadableFileTypes: String, mUploadableFileTitle: String?, mCapturePictureEnable: Boolean) {
        this.mUploadableFileTypes = mUploadableFileTypes
        this.mUploadableFileTitle = mUploadableFileTitle
        this.mCapturePictureEnable = mCapturePictureEnable
    }

    private fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 根据本地调试开关打开Chrome调试
            setWebContentsDebuggingEnabled(WebviewConfig.debugEnable)
        }
        // WebSettings配置
        WebViewSettings.setDefaultWebSettings(this)
        // 前端控制回退栈，默认回退1。
//        mBackStep = 1;
// 重定向保护，防止空白页
//        mRedirectProtected = true;
// 截图使用
        isDrawingCacheEnabled = true
        if (context is Activity) {
            mActivity = WeakReference(context)
        }
        initWebChromeClient()
        initWebViewClient()
    }

    override fun setWebViewClient(client: WebViewClient) {
        mCustomWebViewClient = client
    }

    override fun setWebChromeClient(client: WebChromeClient) {
        mCustomWebChromeClient = client
    }

    fun onDestroy() {
        try {
            (parent as ViewGroup).removeView(this)
        } catch (ignored: Exception) {
        }
        try {
            removeAllViews()
        } catch (ignored: Exception) {
        }
        destroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setGeolocationEnabled(enabled: Boolean) {
        if (enabled) {
            settings.javaScriptEnabled = true
            settings.setGeolocationEnabled(true)
            setGeolocationDatabasePath()
        }
        mGeolocationEnabled = enabled
    }

    @SuppressLint("NewApi")
    protected fun setGeolocationDatabasePath() {
        val activity: Activity?
        activity = if (mFragment != null && mFragment!!.get() != null && null != mFragment!!.get()!!.activity) {
            mFragment!!.get()!!.activity
        } else if (mActivity != null && mActivity!!.get() != null) {
            mActivity!!.get()
        } else {
            return
        }
        settings.setGeolocationDatabasePath(activity!!.filesDir.path)
    }

    /**
     * 初始化WebChromeClient，覆写其中一部分方法
     */
    private fun initWebChromeClient() {
        setWebChromeClient(object : WebChromeClient() {
            // 文件上传回调 (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
            // 文件上传回调 (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
            // 文件上传回调 (Android 2.2 (API level 8) -- Android 2.3 (API level 10)) (hidden method)

            fun openFileChooser(uploadMsg: ValueCallback<Uri?>?, acceptType: String?, capture: String?) {
                openFileInput(uploadMsg, null, false)
            }

            // 文件上传回调 （For Android >= 5.0 ）
            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                return if (Build.VERSION.SDK_INT >= 21) {
                    val allowMultiple = fileChooserParams.mode == FileChooserParams.MODE_OPEN_MULTIPLE
                    openFileInput(null, filePathCallback, allowMultiple)
                    true
                } else {
                    false
                }
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onProgressChanged(view, newProgress)
                } else {
                    super.onProgressChanged(view, newProgress)
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onReceivedTitle(view, title)
                } else {
                    super.onReceivedTitle(view, title)
                }
            }

            override fun onReceivedIcon(view: WebView, icon: Bitmap) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onReceivedIcon(view, icon)
                } else {
                    super.onReceivedIcon(view, icon)
                }
            }

            override fun onReceivedTouchIconUrl(view: WebView, url: String, precomposed: Boolean) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onReceivedTouchIconUrl(view, url, precomposed)
                } else {
                    super.onReceivedTouchIconUrl(view, url, precomposed)
                }
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onShowCustomView(view, callback)
                } else {
                    super.onShowCustomView(view, callback)
                }
            }

            @SuppressLint("NewApi")
            override fun onShowCustomView(view: View, requestedOrientation: Int, callback: CustomViewCallback) {
                if (Build.VERSION.SDK_INT >= 14) {
                    if (mCustomWebChromeClient != null) {
                        mCustomWebChromeClient!!.onShowCustomView(view, requestedOrientation, callback)
                    } else {
                        super.onShowCustomView(view, requestedOrientation, callback)
                    }
                }
            }

            override fun onHideCustomView() {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onHideCustomView()
                } else {
                    super.onHideCustomView()
                }
            }

            override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                } else {
                    super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                }
            }

            override fun onRequestFocus(view: WebView) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onRequestFocus(view)
                } else {
                    super.onRequestFocus(view)
                }
            }

            override fun onCloseWindow(window: WebView) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onCloseWindow(window)
                } else {
                    super.onCloseWindow(window)
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onJsAlert(view, url, message, result)
                } else {
                    super.onJsAlert(view, url, message, result)
                }
            }

            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onJsConfirm(view, url, message, result)
                } else {
                    super.onJsConfirm(view, url, message, result)
                }
            }

            override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onJsPrompt(view, url, message, defaultValue, result)
                } else {
                    super.onJsPrompt(view, url, message, defaultValue, result)
                }
            }

            override fun onJsBeforeUnload(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onJsBeforeUnload(view, url, message, result)
                } else {
                    super.onJsBeforeUnload(view, url, message, result)
                }
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                if (mGeolocationEnabled) {
                    callback.invoke(origin, true, false)
                } else {
                    if (mCustomWebChromeClient != null) {
                        mCustomWebChromeClient!!.onGeolocationPermissionsShowPrompt(origin, callback)
                    } else {
                        super.onGeolocationPermissionsShowPrompt(origin, callback)
                    }
                }
            }

            override fun onGeolocationPermissionsHidePrompt() {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onGeolocationPermissionsHidePrompt()
                } else {
                    super.onGeolocationPermissionsHidePrompt()
                }
            }

            @SuppressLint("NewApi")
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    if (mCustomWebChromeClient != null) {
                        mCustomWebChromeClient!!.onPermissionRequest(request)
                    } else {
                        super.onPermissionRequest(request)
                    }
                }
            }

            @SuppressLint("NewApi")
            override fun onPermissionRequestCanceled(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    if (mCustomWebChromeClient != null) {
                        mCustomWebChromeClient!!.onPermissionRequestCanceled(request)
                    } else {
                        super.onPermissionRequestCanceled(request)
                    }
                }
            }

            override fun onJsTimeout(): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onJsTimeout()
                } else {
                    super.onJsTimeout()
                }
            }

            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onConsoleMessage(message, lineNumber, sourceID)
                } else {
                    super.onConsoleMessage(message, lineNumber, sourceID)
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onConsoleMessage(consoleMessage)
                } else {
                    super.onConsoleMessage(consoleMessage)
                }
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.defaultVideoPoster
                } else {
                    super.getDefaultVideoPoster()
                }
            }

            override fun getVideoLoadingProgressView(): View? {
                return if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.videoLoadingProgressView
                } else {
                    super.getVideoLoadingProgressView()
                }
            }

            override fun getVisitedHistory(callback: ValueCallback<Array<String>>) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.getVisitedHistory(callback)
                } else {
                    super.getVisitedHistory(callback)
                }
            }

            override fun onExceededDatabaseQuota(url: String, databaseIdentifier: String, quota: Long, estimatedDatabaseSize: Long, totalQuota: Long, quotaUpdater: QuotaUpdater) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater)
                } else {
                    super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater)
                }
            }

            override fun onReachedMaxAppCacheSize(requiredStorage: Long, quota: Long, quotaUpdater: QuotaUpdater) {
                if (mCustomWebChromeClient != null) {
                    mCustomWebChromeClient!!.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater)
                } else {
                    super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater)
                }
            }
        })
    }

    /**
     * 初始化WebViewClient，覆写其中一部分方法
     */
    private fun initWebViewClient() {
        setWebViewClient(object : WebViewClient() {
            /**
             * 页面加载出现错误
             * 排除几种误判：
             * 1. 加载失败的url跟WebView里的url不是同一个url，排除；
             * 2. rrorCode=-1，表明是ERROR_UNKNOWN的错误，为了保证不误判，排除
             * 3. failingUrl=null&errorCode=-12，由于错误的url是空而不是ERROR_BAD_URL，排除
             * @param view
             */
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) { // -12 == EventHandle.ERROR_BAD_URL, a hide return code inside android.net.http package
                if (failingUrl != null && failingUrl != view.url && failingUrl != view.originalUrl /* not subresource error*/
                        || failingUrl == null && errorCode != -12 /*not bad url*/
                        || errorCode == -1) { //当 errorCode = -1 且错误信息为 net::ERR_CACHE_MISS
                    return
                }
                if (!TextUtils.isEmpty(failingUrl)) {
                    if (failingUrl == view.url) {
                        Toast.makeText(view.context, R.string.link_load_error, Toast.LENGTH_SHORT).show()
                    }
                }
                if (mListener != null) {
                    mListener!!.onPageError(errorCode, description, failingUrl)
                }
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onReceivedError(view, errorCode, description, failingUrl)
                }
            }

            /**
             * 页面HTTPS请求，遇到SSL错误时
             */
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onReceivedSslError(view, handler, error)
                } else {
                    if (BuildConfig.DEBUG) {
                        handler.proceed()
                    } else {
                        super.onReceivedSslError(view, handler, error)
                    }
                }
            }

            /**
             * URL重定向相关
             * @param view
             * @param url
             * @return
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (!isLoadingFinished) {
                    isRedirect = true
                }
                isLoadingFinished = false
                if (mCustomWebViewClient != null) {
                    if (mCustomWebViewClient!!.shouldOverrideUrlLoading(view, url)) {
                        return true
                    }
                }
                // 过滤非法字符
                if (!isPermittedUrl(url)) {
                    if (mListener != null) {
                        mListener!!.onExternalPageRequest(url)
                    }
                    return true
                }
                // 打开一些特定页面
                if (url.startsWith("tel:")) {
                    try {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                        view.context.startActivity(intent)
                    } catch (ex: Exception) {
                        Logger.e("DxyWebView", ex.message)
                    }
                    return true
                } else if (url.startsWith("mailto:")) {
                    try {
                        val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                        view.context.startActivity(i)
                    } catch (ex: Exception) {
                        Logger.e("DxyWebView", ex.message)
                    }
                    return true
                } else if (url.contains("baiduyun") || url.endsWith(".apk") ||
                        url.startsWith("mqqwpa:") || url.startsWith("intent:")) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        view.context.startActivity(intent)
                    } catch (ex: Exception) {
                        Logger.e("DxyWebView", ex.message)
                    }
                    return true
                }
                return false
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                if (!isRedirect && mListener != null) {
                    mListener!!.onPageStarted(url, favicon)
                }
                isLoadingFinished = false
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onPageStarted(view, url, favicon)
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!isRedirect) {
                    isLoadingFinished = true
                }
                if (isLoadingFinished && !isRedirect) {
                    if (mListener != null) {
                        mListener!!.onPageFinished(url)
                    }
                }
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onPageFinished(view, url)
                }
            }

            override fun onLoadResource(view: WebView, url: String) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onLoadResource(view, url)
                } else {
                    super.onLoadResource(view, url)
                }
            }

            @SuppressLint("NewApi")
            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.shouldInterceptRequest(view, url)
                } else {
                    super.shouldInterceptRequest(view, url)
                }
            }

            @SuppressLint("NewApi")
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return if (Build.VERSION.SDK_INT >= 21) {
                    if (mCustomWebViewClient != null) {
                        mCustomWebViewClient!!.shouldInterceptRequest(view, request)
                    } else {
                        super.shouldInterceptRequest(view, request)
                    }
                } else {
                    null
                }
            }

            override fun onFormResubmission(view: WebView, dontResend: Message, resend: Message) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onFormResubmission(view, dontResend, resend)
                } else {
                    super.onFormResubmission(view, dontResend, resend)
                }
            }

            override fun doUpdateVisitedHistory(view: WebView, url: String, isReload: Boolean) {
                mCustomWebViewClient?.doUpdateVisitedHistory(view, url, isReload) ?: kotlin.run {
                    super.doUpdateVisitedHistory(view, url, isReload)
                }
            }

            @SuppressLint("NewApi")
            override fun onReceivedClientCertRequest(view: WebView, request: ClientCertRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    if (mCustomWebViewClient != null) {
                        mCustomWebViewClient!!.onReceivedClientCertRequest(view, request)
                    } else {
                        super.onReceivedClientCertRequest(view, request)
                    }
                }
            }

            override fun onReceivedHttpAuthRequest(view: WebView, handler: HttpAuthHandler, host: String, realm: String) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onReceivedHttpAuthRequest(view, handler, host, realm)
                } else {
                    super.onReceivedHttpAuthRequest(view, handler, host, realm)
                }
            }

            override fun shouldOverrideKeyEvent(view: WebView, event: KeyEvent): Boolean {
                return if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.shouldOverrideKeyEvent(view, event)
                } else {
                    super.shouldOverrideKeyEvent(view, event)
                }
            }

            override fun onUnhandledKeyEvent(view: WebView, event: KeyEvent) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onUnhandledKeyEvent(view, event)
                } else {
                    super.onUnhandledKeyEvent(view, event)
                }
            }

            override fun onScaleChanged(view: WebView, oldScale: Float, newScale: Float) {
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient!!.onScaleChanged(view, oldScale, newScale)
                } else {
                    super.onScaleChanged(view, oldScale, newScale)
                }
            }
        })
    }

    /**
     * 过滤部分的URL
     *
     * @param url
     * @return
     */
    fun isPermittedUrl(url: String?): Boolean {
        val parsedUrl = Uri.parse(url)
        val actualHost = parsedUrl.host ?: return false
        // 如果Host包含非法字符
        if (!actualHost.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%\\[\\]-]*$".toRegex())) {
            return false
        }
        val actualUserInformation = parsedUrl.userInfo
        // 如果userInfo包含非法字符
        return if (actualUserInformation != null && !actualUserInformation.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%-]*$".toRegex())) {
            false
        } else false
    }

    @SuppressLint("NewApi")
    protected fun openFileInput(fileUploadCallbackFirst: ValueCallback<Uri?>?, fileUploadCallbackSecond: ValueCallback<Array<Uri>>?, allowMultiple: Boolean) {
        mFileUploadCallbackFirst?.onReceiveValue(null)
        mFileUploadCallbackFirst = fileUploadCallbackFirst
        mFileUploadCallbackSecond?.onReceiveValue(null)
        mFileUploadCallbackSecond = fileUploadCallbackSecond
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        if (allowMultiple) {
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        i.type = mUploadableFileTypes
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, i)
        if (mUploadableFileTitle != null) {
            chooserIntent.putExtra(Intent.EXTRA_TITLE, mUploadableFileTitle)
        }
        if (mCapturePictureEnable) {
            val takePicIntent = createTakePicture()
            val intentArray = takePicIntent?.let { arrayOf(it) } ?: arrayOf()
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        }
        if (mFragment?.get() != null) {
            mFragment!!.get()!!.startActivityForResult(chooserIntent, mRequestCodeFilePicker)
        } else if (mActivity != null && mActivity!!.get() != null) {
            mActivity!!.get()!!.startActivityForResult(chooserIntent, mRequestCodeFilePicker)
        }
    }

    /**
     * 生成拍摄照片的Intent
     *
     * @return
     */
    private fun createTakePicture(): Intent? {
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent?.resolveActivity(this@CommonWebView.context.packageManager) != null) { // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                Logger.e("DxyWebView", "Unable to create Image File")
            }
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile))
            } else {
                takePictureIntent = null
            }
        }
        return takePictureIntent
    }

    /**
     * 生成拍摄照片存放的File
     *
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createImageFile(): File { // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == mRequestCodeFilePicker) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst!!.onReceiveValue(intent.data)
                        mFileUploadCallbackFirst = null
                    } else if (mFileUploadCallbackSecond != null) {
                        var dataUris: Array<Uri> = arrayOf()
                        try {
                            if (intent.dataString != null) {
                                dataUris = arrayOf(Uri.parse(intent.dataString))
                            } else {
                                intent.clipData?.let { clipData ->
                                    for (i in 0 until clipData.itemCount) {
                                        dataUris[i] = clipData.getItemAt(i).uri
                                    }
                                }
                            }
                        } catch (ignored: Exception) {
                        }
                        mFileUploadCallbackSecond!!.onReceiveValue(dataUris)
                        mFileUploadCallbackSecond = null
                    }
                }
            } else {
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst!!.onReceiveValue(null)
                    mFileUploadCallbackFirst = null
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond!!.onReceiveValue(null)
                    mFileUploadCallbackSecond = null
                }
            }
        }
    }

}