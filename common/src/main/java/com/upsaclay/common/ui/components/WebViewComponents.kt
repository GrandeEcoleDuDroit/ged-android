package com.upsaclay.common.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun StandardWebView(
    url: String,
){
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
        },
        update = {  webView ->
            webView.loadUrl(url)
        }
    )
}