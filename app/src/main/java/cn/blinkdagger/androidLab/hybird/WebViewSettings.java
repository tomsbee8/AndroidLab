package cn.blinkdagger.androidLab.hybird;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */
public class WebViewSettings {

    /**
     * 常用的默认设置
     *
     * @param webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void setDefaultWebSettings(WebView webView) {
        android.webkit.WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(webView.getContext().getDir("appCache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);

        //设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 不保存密码,密码会被明文保到 /data/data/com.package.name/databases/webview.db 中
        webSettings.setSavePassword(false);
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces(webView);

    }

    /**
     * 如果启用了JavaScript，移除相关interface防止远程执行漏洞
     *
     * @param webView
     */
    private static void removeJavascriptInterfaces(WebView webView) {
        //移除不安全接口
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }

    /**
     * 文件访问漏洞解决方案:
     * 1. 对于不需要使用 file 协议的应用，禁用 file 协议；
     * 2. 对于还需要使用 file 协议的应用，禁止 file 协议加载 JavaScript。
     * @param fileAccess 是否允许加载本地 html 文件
     * @param url 要加载的URL
     */
    protected static void setAllowFileAccess(WebSettings webSettings, boolean fileAccess, String url) {
        if (fileAccess) {
            webSettings.setAllowFileAccess(false);//设置为 false 将不能
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowFileAccessFromFileURLs(false);
                webSettings.setAllowUniversalAccessFromFileURLs(false);
            }
        } else {
            webSettings.setAllowFileAccess(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowFileAccessFromFileURLs(false);
                webSettings.setAllowUniversalAccessFromFileURLs(false);
            }
            // 禁止 file 协议加载 JavaScript
            if (url.startsWith("file://")) {
                webSettings.setJavaScriptEnabled(false);
            } else {
                webSettings.setJavaScriptEnabled(true);
            }
            webSettings.setAllowFileAccess(true);//设置为 false 将不能加载本地 html 文件
        }
    }
}
