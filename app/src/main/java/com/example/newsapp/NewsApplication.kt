package com.example.newsapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsapp.presentation.notification.NewsNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NewsNotificationWorker>(
                1,
                TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "news_notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
    }
}
