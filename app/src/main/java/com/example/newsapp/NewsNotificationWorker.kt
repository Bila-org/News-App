package com.example.newsapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NewsNotificationWorker (
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        Log.d("Worker", "doWork executed")
        return if(canShowNotification()){
            try {
                showNotification()
                Log.d("Worker", "Notification shown successfully")
                Result.success()
            } catch (e: Exception) {
                Log.e("Worker","Error showing notification: ${e.message}")
                Result.failure()
            }
        }else {
            Log.d("Worker", "Notification permission not granted")
            Result.success()
        }
    }

    private fun showNotification() {
        val channelId = "news_channel"
        val notificationId = 1

        createNotificationChannel(channelId)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Latest News Updates")
            .setContentText("Check out today's top stories!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
//            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "News Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "News notifications"
//                enableVibration(true)
//                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun canShowNotification(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationManager.areNotificationsEnabled()
        } else true
    }


}