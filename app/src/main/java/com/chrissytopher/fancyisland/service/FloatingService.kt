package com.chrissytopher.fancyisland.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.chrissytopher.fancyisland.FOREGROUND_SERVICE_ID
import com.chrissytopher.fancyisland.MainActivity
import com.chrissytopher.fancyisland.NEW_NOTIFICATION
import com.chrissytopher.fancyisland.NOTIFICATION_CHANNEL
import com.chrissytopher.fancyisland.OVERLAY_COMMAND_INTENT
import com.chrissytopher.fancyisland.START_OVERLAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class FloatingService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    // todo make private
    lateinit var overlayController: OverlayController
    var is_started = false

    override fun onCreate() {
        super.onCreate()
        overlayController = OverlayController(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val command = intent.getStringExtra(OVERLAY_COMMAND_INTENT)
            when (command) {
                START_OVERLAY -> {
                    is_started = true
                    startForeground(FOREGROUND_SERVICE_ID, buildNotification())
                    return START_STICKY
                }
                NEW_NOTIFICATION -> {
                    if (!is_started) {
                        is_started = true
                        startForeground(FOREGROUND_SERVICE_ID, buildNotification())
                    }
                    overlayController.setupNotification(intent.getIntExtra("notification_id", -1))
                }
                else -> {}
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun buildNotification(): Notification {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).setContentTitle("Fancy Island is running")
                .setSmallIcon(androidx.core.R.drawable.notification_action_background).setContentIntent(pendingIntent).build()
        return notification
    }
}