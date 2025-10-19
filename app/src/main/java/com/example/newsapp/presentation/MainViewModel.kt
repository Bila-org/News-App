package com.example.newsapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private val _notificationPermissionState = mutableStateOf(false)
    val notificationPermissionState = _notificationPermissionState


    fun onPermissionResult(isGranted: Boolean) {
        _notificationPermissionState.value = isGranted
    }

}