package com.example.newsapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.newsapp.presentation.MainViewModel
import com.example.newsapp.presentation.notification.NewsNotificationWorker
import com.example.newsapp.presentation.ui.NewsAppMain
import com.example.newsapp.ui.theme.NewsAppTheme
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        mainViewModel.onPermissionResult(isGranted)
        if (isGranted)
            scheduleNotifications()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        mainViewModel.onPermissionResult(isGranted)

        setContent {
            NewsAppTheme() {
                val notificationPermissionState = mainViewModel.notificationPermissionState

                NewsAppMain(
                    onNotificationEnableClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(
                                    this@MainActivity,
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            }
                        } else {
                            mainViewModel.onPermissionResult(true)  // Permission granted by default below android 13
                            scheduleNotifications()
                        }
                    },
                    isNotificationEnabled = notificationPermissionState.value,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun scheduleNotifications() {

        val workManager = WorkManager.getInstance(this)
        lifecycleScope.launch {
            val future: ListenableFuture<List<WorkInfo>> =
                workManager.getWorkInfosForUniqueWork("news_notification")

            val workInfos = future.get()
            if (workInfos.isNotEmpty())
                return@launch

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
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

            workManager.enqueueUniquePeriodicWork(
                "news_notification",
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationWorkRequest
            )
        }
    }
}