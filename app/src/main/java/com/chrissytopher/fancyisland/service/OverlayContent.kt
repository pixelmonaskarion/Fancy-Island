package com.chrissytopher.fancyisland.service

import android.app.Notification
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@OptIn(ExperimentalUnitApi::class)
@Composable
fun OverlayContent(overlayState: OverlayState) {
  Log.d("FancyIsland", "rendering content")
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Box(modifier = Modifier.background(Color.White).fillMaxSize()) {
      Column {
        Text(text = "Notifications", fontSize = TextUnit(20.0F, TextUnitType.Sp))
        LazyColumn() {
          items(overlayState.notifications) { sbn ->
            val notification: Notification = sbn.notification
            val extras = notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            if (title != null) {
              Text(text = title, fontSize = TextUnit(20.0F, TextUnitType.Sp))
            }
          }
        }
      }
    }
  }
}