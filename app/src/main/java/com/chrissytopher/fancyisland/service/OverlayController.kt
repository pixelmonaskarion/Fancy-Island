package com.chrissytopher.fancyisland.service

import android.app.Service
import android.content.Context
import android.content.Context.INPUT_SERVICE
import android.graphics.PixelFormat
import android.hardware.input.InputManager
import android.os.Build
import android.os.Handler
import android.service.notification.StatusBarNotification
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.chrissytopher.fancyisland.OverlayViewHolder
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


class OverlayController(val service: Service) {
  var viewHolder: OverlayViewHolder? = null
  val state = OverlayState()

  private fun createFullscreenOverlay(): OverlayViewHolder {
    val fullscreenOverlay = OverlayViewHolder(
      WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
      ), service
    )

    // https://developer.android.com/reference/android/view/WindowManager.LayoutParams#MaximumOpacity
    fullscreenOverlay.params.alpha = 1f
    val inputManager = service.applicationContext.getSystemService(INPUT_SERVICE) as InputManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//      logd("inputManager max opacity ${inputManager.maximumObscuringOpacityForTouch}")
      fullscreenOverlay.params.alpha = inputManager.maximumObscuringOpacityForTouch
    }

    fullscreenOverlay.view.setContent {
      CompositionLocalProvider {
        OverlayContent(state)
      }
    }

    return fullscreenOverlay
  }

  fun addView() {
    state.is_active = true
    val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    viewHolder = createFullscreenOverlay()
    windowManager.addView(viewHolder!!.view, viewHolder!!.params)
  }

  fun removeView() {
    state.is_active = false
    val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.removeView(viewHolder!!.view)
    viewHolder!!.view.disposeComposition()
  }

  fun addNotification(notification: StatusBarNotification) {
    state.notifications.add(notification)
    Log.d("FancyIsland", "setting up notification ${notification.id}")
    Thread {
      Log.d("FancyIsland", "waiting...")
      Thread.sleep(3000)
      Log.d("FancyIsland", "done waiting")
      state.notifications.remove(notification)
    }.start()
    Log.d("FancyIsland", "async check")
  }
}