package com.example.newsapp.presentation.common.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri


object OpenArticleInBrowser {
    fun launch(context: Context, articleUrl: String) {
        try {
            openCustomTab(context, articleUrl)
        } catch (e: ActivityNotFoundException) {
            openArticleInBrowserUsingIntent(context, articleUrl)
        }
    }
}

fun openCustomTab(context: Context, articleUrl: String) {
    CustomTabsIntent.Builder().build().apply {
        launchUrl(context, articleUrl.toUri())
    }
}

fun openArticleInBrowserUsingIntent(context: Context, articleUrl: String) {
    Intent(Intent.ACTION_VIEW).also {
        it.data = articleUrl.toUri()
        if (it.resolveActivity(context.packageManager) != null) {
            context.startActivity(it)
        }
    }
}