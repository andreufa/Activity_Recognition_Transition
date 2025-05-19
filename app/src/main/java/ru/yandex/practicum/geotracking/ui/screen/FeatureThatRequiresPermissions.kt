package ru.yandex.practicum.geotracking.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ru.yandex.practicum.geotracking.R
import ru.yandex.practicum.geotracking.ui.theme.GeotrackingTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresPermissions(isPermissionGranted: @Composable () -> Unit) {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    )
    if (permissionsState.allPermissionsGranted) {
        isPermissionGranted.invoke()
    } else {
        ShowScreenRationalePermission(permissionsState)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowScreenRationalePermission(permissionsState: MultiplePermissionsState) {
    val textToShow = if (permissionsState.shouldShowRationale) {
        stringResource(R.string.permission_rationale)
    } else {
        stringResource(R.string.permission_rationale)
    }
    PermissionTextBox(textToShow) { permissionsState.launchMultiplePermissionRequest() }
}

@Composable
fun PermissionTextBox(textToShow: String, permissionLauncher: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(textToShow)
            }
            Spacer(modifier = Modifier.size(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    permissionLauncher.invoke()
                }) {
                    Text(stringResource(R.string.grant_permissions))
                }
            }
        }
    }
}

@Composable
@Preview(name = "Light", showBackground = true)
fun FakeRationalePermission() {
    GeotrackingTheme {
        PermissionTextBox(stringResource(R.string.permission_rationale)) { }
    }
}



