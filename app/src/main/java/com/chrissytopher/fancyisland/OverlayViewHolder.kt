package com.chrissytopher.fancyisland

import android.app.Service
import android.view.Gravity
import android.view.WindowManager
import com.chrissytopher.fancyisland.service.overlayViewFactory

class OverlayViewHolder(val params: WindowManager.LayoutParams, service: Service) {
  val view = overlayViewFactory(service)

  init {
    params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
  }
}