package com.example.activitybroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

//@RunWith(AndroidJUnit4::class)
class ActivityRecognizedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        context.startForegroundService(Intent(context, HelloService::class.java))
        Log.i("ActivityRecognition", "in")
        if (ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            if (result != null) {
                handleDetectedActivities(result.probableActivities)
            }else{
                Log.i("ActivityRecognition", "Eroooor in extractResult(intent)")
            }
        }else{
            Log.i("ActivityRecognition", "Eroooor in ActivityRecognitionResult.hasResult(intent)")
        }
    }

    private fun handleDetectedActivities(probableActivities: List<DetectedActivity>) {
        for (activity in probableActivities) {
            val activityType = getActivityString(activity.type)
            Log.i("ActivityRecognition", "Activity: $activityType, Confidence: ${activity.confidence}")
        }
    }

    private fun getActivityString(detectedActivityType: Int): String {
        return when (detectedActivityType) {
            DetectedActivity.IN_VEHICLE -> "In Vehicle"
            DetectedActivity.ON_BICYCLE -> "On Bicycle"
            DetectedActivity.ON_FOOT -> "On Foot"
            DetectedActivity.RUNNING -> "Running"
            DetectedActivity.STILL -> "Still"
            DetectedActivity.TILTING -> "Tilting"
            DetectedActivity.WALKING -> "Walking"
            DetectedActivity.UNKNOWN -> "Unknown"
            else -> "Other"
        }
    }
}
