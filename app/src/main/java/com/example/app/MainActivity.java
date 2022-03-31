package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 业务背景：JS通知Native包名，Native判断对应app是否安装，并通知JS
 */
public class MainActivity extends Activity {

    private WebView mWebView;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        findViewById(R.id.btn).setOnClickListener(
                v ->
                        //native通知JS，并回调给native
                        mWebView.evaluateJavascript("javascript:nativeCallJS('true')",
                                value -> Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show()));
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult jsPromptResult) {
                //JS执行prompt后
                Log.d("yue_", "message from JS : " + message);
                if (!TextUtils.isEmpty(message)) {
                    //Native 通知 JS
                    jsPromptResult.confirm("hello JS");
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, jsPromptResult);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("yue_", "consoleMessage: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });
        // REMOTE RESOURCE
        // mWebView.loadUrl("https://example.com");

        // LOCAL RESOURCE
        mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
