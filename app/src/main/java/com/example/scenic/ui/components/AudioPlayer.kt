package com.example.scenic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlayer(
    isPlaying: Boolean,
    isLooping: Boolean,
    isMuted: Boolean,
    volume: Float,
    onPlayPauseClick: () -> Unit,
    onLoopClick: () -> Unit,
    onMuteClick: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Play/Pause
        IconButton(onClick = onPlayPauseClick) {
            Icon(
                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }

        // Loop
        IconButton(onClick = onLoopClick) {
            Icon(
                imageVector = Icons.Filled.Repeat, 
                contentDescription = if (isLooping) "Unloop" else "Loop",
                tint = if (isLooping) Color.Blue else Color.Gray // Highlight when active
            )
        }

        // Mute/Volume
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMuteClick) {
                Icon(
                    imageVector = if (isMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute"
                )
            }
            
            Slider(
                value = if (isMuted) 0f else volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f, fill = false).width(100.dp) 
            )
        }
    }
}
