package ru.yandex.practicum.geotracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class MotionBroadcastReceiver(
    private val dispatchResult: (ActivityTransitionResult) -> Unit,
    private val transition: (Int) -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)!!
            dispatchResult.invoke(result)
            for (event in result.transitionEvents) {
                if (event.activityType == DetectedActivity.WALKING) {
                    transition.invoke(event.transitionType)
                }
            }
        }
    }
}
