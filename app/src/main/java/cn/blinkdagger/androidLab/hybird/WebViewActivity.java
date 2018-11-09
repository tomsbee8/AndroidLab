package cn.blinkdagger.androidLab.hybird;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */

public class WebViewActivity extends BaseActivity implements CommonWebView.LoadListener {
    private CommonWebView dxyWebView;
    private ProgressBar progressBar;
    private FrameLayout mRootView;
    private View mCustomView; // 视频播放View

    //原始的url
    private String url;
    private int mCurrentProgress;

    //重定向的url，用于登陆成功后
    private String mTargetUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web_view);
        initView();
        initData();

    }

    @Override
    protected void initView() {
        dxyWebView = findViewById(R.id.dxy_webView);
        progressBar = findViewById(R.id.dxy_wv_pb);
        mRootView = findViewById(R.id.dxy_wv_root);

        registerForContextMenu(dxyWebView);

        dxyWebView.setBackgroundColor(getResources().getColor(R.color.color_f4f4f4));

        String title = getIntent().getStringExtra("title");
        setTitle(title);

    }


    @Override
    protected void initData() {
        // cookie

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        dxyWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        dxyWebView.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (dxyWebView != null && dxyWebView.canGoBack()) {
            dxyWebView.goBack();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        dxyWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_common_web_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        dxyWebView.onActivityResult(requestCode, resultCode, intent);
    }

    private void initWebView() {
        dxyWebView.setUploadFileParam("image/*", "选择图片", true);
        dxyWebView.setListener(this, this);

        // 如果在没有登录的状态下，则 WebView 不需要缓存
//        if (!UserManager.isLogin()) {
//            dxyWebView.clearHistory();
//            dxyWebView.clearFormData();
//            dxyWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        }
        dxyWebView.clearCache(true);
        dxyWebView.requestFocus();
        dxyWebView.loadUrl(url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        mCurrentProgress = progressBar.getProgress();
        startProgressAnimation(100, true);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    private void startProgressAnimation(int newProgress, final boolean gone) {
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", mCurrentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (gone) {
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        animator.start();
    }

    /**
     *
     */
    class CustomWebChromeClient extends WebChromeClient {

        private boolean isStart = false;


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mCurrentProgress = progressBar.getProgress();
            startProgressAnimation(newProgress, false);
            if (newProgress == 100) {
                if (!isStart) {
                    isStart = true;
                }
            }
        }

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mRootView.addView(mCustomView);
            dxyWebView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onHideCustomView() {
            dxyWebView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mRootView.removeView(mCustomView);
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

    }
}
