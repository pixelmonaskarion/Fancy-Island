package com.chrissytopher.fancyisland

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.view.WindowManager
import com.chrissytopher.fancyisland.service.OverlayController


class NotificationListener : NotificationListenerService() {
    var overlayController = OverlayController(this)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("FancyIsland", "received notification")
        if (!overlayController.state.is_active) {
            overlayController.addView()
        }
        overlayController.addNotification(sbn)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("FancyIsland", "starting notification listener")
        overlayController.addView()

    }
}