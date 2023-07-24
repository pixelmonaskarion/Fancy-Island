package com.chrissytopher.fancyisland

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.chrissytopher.fancyisland.ui.theme.FancyIslandTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + this.applicationContext.packageName)
            )
            ActivityCompat.startActivityForResult(
                this as Activity, intent, REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION, null
            )
        }
        if (!isNotificationServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
        Log.d("FancyIsland", "registering receiver")
        setContent {
            FancyIslandTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { })
                LaunchedEffect(Unit) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }

                Button(onClick = {
//                    startOverlayService(this.applicationContext)
                    moveTaskToBack(true)
                }) {
                    Text(text = "Start Overlay")
                }
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName: String = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(packageName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FancyIslandTheme {
        Greeting("Android")
    }
}

//fun startOverlayService(context: Context) {
//    val intent = Intent(context.applicationContext, FloatingService::class.java)
//    intent.putExtra(OVERLAY_COMMAND_INTENT, START_OVERLAY)
//    context.startForegroundService(intent)
//}
//
//fun newNotification(context: Context, originalIntent: Intent) {
//    val intent = Intent(context.applicationContext, FloatingService::class.java)
//    intent.replaceExtras(originalIntent)
//    intent.putExtra(OVERLAY_COMMAND_INTENT, NEW_NOTIFICATION)
//    context.startForegroundService(intent)
//}
