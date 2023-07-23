package com.chrissytopher.fancyisland.service

import android.content.Context
import android.content.Context.INPUT_SERVICE
import android.graphics.PixelFormat
import android.hardware.input.InputManager
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.chrissytopher.fancyisland.OverlayViewHolder
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


class OverlayController(val service: FloatingService) {
  val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  var viewHolder: OverlayViewHolder? = null
  companion object {
    val state = OverlayState()
  }

  init {
    Log.d("FancyIsland", "OverlayController init")
    addView()
  }

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

  private fun addView() {
    viewHolder = createFullscreenOverlay()
    windowManager.addView(viewHolder!!.view, viewHolder!!.params)
  }

  private fun removeView() {
    windowManager.removeView(viewHolder!!.view)
    viewHolder!!.view.disposeComposition()
  }

  fun setupNotification(id: Int) {
    Log.d("FancyIsland", "setting up notification $id")
    viewHolder!!.view.findViewTreeLifecycleOwner()!!.lifecycleScope.launch {
      Log.d("FancyIsland", "waiting...")
      Thread.sleep(3000)
      Log.d("FancyIsland", "done waiting")
      for (i in 0 until state.notifications.size) {
        if (state.notifications[i].id == id) {
          Log.d("FancyIsland", "removed notification $id")
          state.notifications.removeAt(i)
          break
        }
      }
      if (state.notifications.size < 1) {
        removeView()
        service.is_started = false
        service.stopSelf()
      }
    }
    Log.d("FancyIsland", "async check")
  }
}