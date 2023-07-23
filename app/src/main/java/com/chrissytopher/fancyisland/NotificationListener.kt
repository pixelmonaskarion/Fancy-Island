package com.chrissytopher.fancyisland

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.chrissytopher.fancyisland.service.OverlayController


class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("FancyIsland","received notification")
        val intent = Intent(ACTION)
        OverlayController.state.notifications.add(sbn)
        intent.putExtra("notification_id", sbn.id)
        sendBroadcast(intent)
    }

    companion object {
        const val ACTION = "com.chrissytopher.fancy_island.ACTION_NOTIFY"
    }
}