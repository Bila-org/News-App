package com.example.newsapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsapp.presentation.notification.NewsNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val calendar = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)
            if(before(Calendar.getInstance())){
                add(Calendar.DATE, 1)
            }
        }
        val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

        val notificationWorkRequest = PeriodicWorkRequestBuilder<NewsNotificationWorker>(
            1,
            TimeUnit.DAYS,
        )
            .setConstraints(constraints)
//            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS) // comment for testing
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "news_notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
    }
}
