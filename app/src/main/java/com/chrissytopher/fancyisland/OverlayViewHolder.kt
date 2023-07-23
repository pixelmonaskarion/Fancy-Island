package com.chrissytopher.fancyisland

import android.view.Gravity
import android.view.WindowManager
import com.chrissytopher.fancyisland.service.FloatingService
import com.chrissytopher.fancyisland.service.overlayViewFactory

class OverlayViewHolder(val params: WindowManager.LayoutParams, service: FloatingService) {
  val view = overlayViewFactory(service)

  init {
    params.gravity = Gravity.TOP or Gravity.LEFT
  }
}