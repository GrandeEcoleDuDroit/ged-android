package com.upsaclay.core.ui.components

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
            }
        },
        update = {  webView ->
            webView.loadUrl(url)
        }
    )
}