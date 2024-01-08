package com.example.activitybroadcast

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.activitybroadcast.ui.theme.ActivityBroadCastTheme
import com.google.android.gms.location.ActivityRecognition


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBroadCastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            startActivityRecognition()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_ACTIVITY_RECOGNITION)
        }
    }

    private fun startActivityRecognition() {
        val activityRecognitionClient = ActivityRecognition.getClient(this)
        val intent = Intent(this, ActivityRecognizedReceiver::class.java)

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, pendingIntentFlags)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("ActivityRecognition", "Eroooor in ActivityCompat.checkSelfPermission")
            return
        }
        activityRecognitionClient.requestActivityUpdates(3000, pendingIntent)
        Log.i("ActivityRecognition", "activityRecognitionClient")
        this.startForegroundService(Intent(this, HelloService::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startActivityRecognition()
            } else {
                Log.i("ActivityRecognition", "Permission denied")
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 100
    }
}


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ActivityBroadCastTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//
//        val activityRecognitionClient = ActivityRecognition.getClient(this)
//        val intent = Intent(this, ActivityRecognizedReceiver::class.java)
//
//        // Specify FLAG_IMMUTABLE or FLAG_MUTABLE based on your requirement
//        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        } else {
//            PendingIntent.FLAG_UPDATE_CURRENT
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, pendingIntentFlags)
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACTIVITY_RECOGNITION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            Log.i("ActivityRecognition", "Eroooor in ActivityCompat.checkSelfPermission")
//            return
//        }
//        activityRecognitionClient.requestActivityUpdates(1, pendingIntent)
//    }
//}

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
    ActivityBroadCastTheme {
        Greeting("Android")
    }
}