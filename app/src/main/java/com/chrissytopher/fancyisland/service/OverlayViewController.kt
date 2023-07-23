package com.chrissytopher.fancyisland.service

import android.util.Log
import android.view.WindowManager
import com.chrissytopher.fancyisland.OverlayViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OverlayViewController(
  val createOverlayViewHolder: () -> OverlayViewHolder,
  val isVisible: Flow<Boolean>,
  val windowManager: WindowManager
) {
  // doitwrong
  init {
    // todo get scope from service
    CoroutineScope(Dispatchers.Main).launch {
      watchIsVisible()
    }
  }

  private suspend fun watchIsVisible() {
    var viewHolder: OverlayViewHolder? = null
    // isVisible first() should always be true
    isVisible.collect { isVisible ->
      if (isVisible) {
        viewHolder = createOverlayViewHolder()
      }
      addOrRemoveView(viewHolder!!, isVisible)
    }
  }

  private fun addOrRemoveView(
    viewHolder: OverlayViewHolder, isVisible: Boolean
  ) {
    when (isVisible) {
      true -> {
        Log.d("FancyIsland", "addview ${viewHolder.view}")
        windowManager.addView(viewHolder.view, viewHolder.params)
      }
      false -> {
        Log.d("FancyIsland", "removeview ${viewHolder.view}")
        // wrap in try catch???
        windowManager.removeView(viewHolder.view)
        viewHolder.view.disposeComposition()
      }
    }
  }
}