package cn.blinkdagger.androidLab.hybird;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cn.blinkdagger.androidLab.R;

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */

public class WebViewFragment extends Fragment implements CommonWebView.LoadListener {

    private final static String PARAM_ARGS = "url";

    private CommonWebView dxyWebView;
    private ProgressBar progressBar;
    private FrameLayout mRootView;
    private View mCustomView;


    private int mCurrentProgress;

    private String url;

    public static WebViewFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(PARAM_ARGS, url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dxy_web_view, container, false);

        dxyWebView = rootView.findViewById(R.id.dxy_webView);
        progressBar = rootView.findViewById(R.id.dxy_wv_pb);

        dxyWebView.setListener(this, this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

    }

    private void initData() {

        url = getArguments().getString(PARAM_ARGS);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        initWebView();
    }

    @Override
    public void onResume() {
        dxyWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        dxyWebView.onPause();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        dxyWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        dxyWebView.onActivityResult(requestCode, resultCode, intent);
    }

    private void initWebView() {
        dxyWebView.setUploadFileParam("image/*", "选择图片", true);
        dxyWebView.setListener(this, this);
        dxyWebView.loadUrl(url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
