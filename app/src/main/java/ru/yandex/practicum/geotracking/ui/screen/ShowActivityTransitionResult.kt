package ru.yandex.practicum.geotracking.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShowActivityTransitionResult(
    result: StateFlow<ActivityTransitionResult?>,
) {
    val state by result.collectAsState()
    Column {
        state?.transitionEvents?.forEach { act ->
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("activity: ${getActivity(act.activityType)}")
                Spacer(Modifier.size(16.dp))
                Text("transition: ${getTransition(act.transitionType)}")
            }
        }
    }
}

fun getTransition(transition: Int?): String {
    if (transition == null) return "NULL"
    return when (transition) {
        ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
        else -> "EXIT"
    }
}

fun getActivity(event: Int?): String {
    if (event == null) return "NULL"
    return when (event) {
        DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
        DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
        DetectedActivity.ON_FOOT -> "ON_FOOT"
        DetectedActivity.STILL -> "STILL"
        DetectedActivity.TILTING -> "TILTING"
        DetectedActivity.WALKING -> "WALKING"
        DetectedActivity.RUNNING -> "RUNNING"
        else -> "UNKNOWN"
    }
}