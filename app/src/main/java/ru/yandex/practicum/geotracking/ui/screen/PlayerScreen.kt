package ru.yandex.practicum.geotracking.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.ActivityTransition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yandex.practicum.geotracking.R

@Composable
fun PlayerScreen(
    motionState: StateFlow<Int>,
    modifier: Modifier = Modifier
){
    val typeMotion by motionState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.blank),
            contentDescription = "Обложка альбома"
        )
        Spacer(Modifier.size(32.dp))
        ButtonPanel(typeMotion == ActivityTransition.ACTIVITY_TRANSITION_EXIT)
    }
}

@Composable
fun ButtonPanel(isExtended:Boolean){
    val scale = if(isExtended) 2f else 4f
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround){
        if(isExtended) {
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.scale(scale)) {
                Icon(
                    imageVector = Icons.Filled.Repeat,
                    contentDescription = "Repeat"
                )
            }
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.scale(scale)) {
                Icon(
                    imageVector = Icons.Filled.SkipPrevious,
                    contentDescription = "Skip preview"
                )
            }
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.scale(scale)) {
                Icon(
                    imageVector = Icons.Filled.SkipNext,
                    contentDescription = "Skip next"
                )
            }
        }
        IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.scale(scale)) {
            Icon(
                imageVector = Icons.Filled.PlayCircleFilled,
                contentDescription = "Play")
        }
        IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.scale(scale)) {
            Icon(
                imageVector = Icons.Filled.Pause,
                contentDescription = "Pause")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewPlayerScreen(){
    PlayerScreen(
        motionState = MutableStateFlow(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
    )
}