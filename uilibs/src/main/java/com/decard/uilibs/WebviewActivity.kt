package com.decard.uilibs

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : AppCompatActivity() {

    private val TAG = "---WebviewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true //开启JavaScript
        // 让JavaScript可以自动打开windows
        settings.setJavaScriptCanOpenWindowsAutomatically(true)
        webView.loadUrl("http://www.baidu.com") //加载本地网页
       //打开网页时不调用系统浏览器， 而是在本WebView中显示：
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url!!)
                return true
            }
        })
    }
}