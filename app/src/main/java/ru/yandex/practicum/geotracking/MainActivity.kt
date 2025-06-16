package ru.yandex.practicum.geotracking

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import kotlinx.coroutines.flow.MutableStateFlow
import ru.yandex.practicum.geotracking.ui.screen.FeatureThatRequiresPermissions
import ru.yandex.practicum.geotracking.ui.screen.PlayerScreen
import ru.yandex.practicum.geotracking.ui.screen.ShowActivityTransitionResult
import ru.yandex.practicum.geotracking.ui.theme.GeotrackingTheme


class MainActivity : ComponentActivity() {
    private val activityState = MutableStateFlow(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
    private val actionState = MutableStateFlow<ActivityTransitionResult?>(null)
    private val motionBroadcastReceiver = MotionBroadcastReceiver(
        dispatchResult = ::dispatchResult
    ) { activity ->
        activityState.value = activity
    }
    private val transitions = listOf(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build(),
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
    )

    private val request = ActivityTransitionRequest(transitions)

    private val motionPendingIntent: PendingIntent by lazy {
        val intent = Intent(TRANSITIONS_RECEIVER_ACTION).apply {
            setPackage(this@MainActivity.packageName)
        }
        PendingIntent.getBroadcast(
            this.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    }

    private fun dispatchResult(result: ActivityTransitionResult) {
        actionState.value = result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.registerReceiver(
            this.applicationContext,
            motionBroadcastReceiver,
            IntentFilter(TRANSITIONS_RECEIVER_ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
        enableEdgeToEdge()
        setContent {
            GeotrackingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FeatureThatRequiresPermissions {
                        Column {
                            Spacer(Modifier.size(48.dp))
                            ShowActivityTransitionResult(
                              result = actionState
                            )
                            PlayerScreen(
                                motionState = activityState,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }


    @RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION)
    override fun onResume() {
        super.onResume()
        val task = ActivityRecognition.getClient(this.applicationContext)
            .requestActivityTransitionUpdates(request, motionPendingIntent)
        task.addOnSuccessListener {
            Log.i(TAG, "Начало отслеживания движения: Успешно")
        }
        task.addOnFailureListener { e: Exception ->
            Log.e(TAG, "Начало отслеживания движения: Ошибка $e")
        }
    }

    @RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION)
    override fun onPause() {
        super.onPause()
        val task = ActivityRecognition.getClient(this)
            .removeActivityTransitionUpdates(motionPendingIntent)
        task.addOnSuccessListener {
            motionPendingIntent.cancel()
            Log.i(TAG, "Остановка отслеживания движения: Успешно")
        }
        task.addOnFailureListener { e: Exception ->
            Log.e(TAG, "Остановка отслеживания движения: Ошибка")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(motionBroadcastReceiver)
    }

    companion object {
        const val TRANSITIONS_RECEIVER_ACTION = "action.ACTIVITY_RECOGNITION"
    }
}

