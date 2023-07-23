package com.chrissytopher.fancyisland.service

import android.service.notification.StatusBarNotification
import androidx.compose.runtime.mutableStateListOf

class OverlayState {
    val notifications = mutableStateListOf<StatusBarNotification>()
}