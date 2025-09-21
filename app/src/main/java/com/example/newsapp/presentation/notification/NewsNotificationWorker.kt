package com.example.newsapp.presentation.notification


import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsapp.MainActivity

class NewsNotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        Log.d("Worker", "doWork executed")
        return if (canShowNotification()) {
            try {
                showNotification()
                Log.d("Worker", "Notification shown successfully")
                Result.success()
            } catch (e: Exception) {
                Log.e("Worker", "Error showing notification: ${e.message}")
                Result.failure()
            }
        } else {
            Log.d("Worker", "Notification permission not granted")
            Result.success()
        }
    }
    private fun showNotification() {
        val channelId = "news_channel"
        val notificationId = 1
        createNotificationChannel(channelId)

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Latest News Updates")
            .setContentText("Check out today's top stories!")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel(channelId: String) {
        val channel = NotificationChannel(
            channelId,
            "News Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "News notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun canShowNotification(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationManager.areNotificationsEnabled()
        } else true
    }
}