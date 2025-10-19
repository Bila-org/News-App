package com.example.newsapp.presentation.common.components

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature


@Composable
fun WebViewScreen(
    articleUrl: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = false
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.safeBrowsingEnabled = true
            webViewClient = WebViewClient()
        }
    }


    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { webView },
        update = { webView.loadUrl(articleUrl) }
    )

    BackHandler(true) {
        if (webView.canGoBack())
            webView.goBack()
        else {
            onBackPressed()
        }
    }

    DisposableEffect(Unit){
        onDispose {
            webView?.let{
                it.stopLoading()
                it.webViewClient = WebViewClient()
                it.clearCache(true)
                it.clearHistory()
            }
        }
    }
}
