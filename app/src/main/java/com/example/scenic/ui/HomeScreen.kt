package com.example.scenic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scenic.ui.components.AudioPlayer
import com.example.scenic.ui.components.ImageRingBook
import com.example.scenic.ui.components.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val images by viewModel.images.collectAsState()
    val audios by viewModel.audios.collectAsState()
    val currentIndex by viewModel.currentImageIndex.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isLooping by viewModel.isLooping.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val transitionInterval by viewModel.transitionInterval.collectAsState()
    val selectedAudioId by viewModel.selectedAudioId.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    // Auto-transition images based on interval
    LaunchedEffect(transitionInterval, images.size, currentIndex) {
        if (images.isNotEmpty()) {
            kotlinx.coroutines.delay(transitionInterval)
            viewModel.nextImage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scenic") },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = viewModel::retry) {
                            Text("Retry")
                        }
                    }
                } else if (images.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No images found.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = viewModel::retry) {
                            Text("Refetch")
                        }
                    }
                } else {
                    ImageRingBook(
                        images = images,
                        currentIndex = currentIndex,
                        onIndexChange = viewModel::setImageIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Take available space
                    )

                    AudioPlayer(
                        isPlaying = isPlaying,
                        isLooping = isLooping,
                        isMuted = isMuted,
                        volume = volume,
                        onPlayPauseClick = viewModel::togglePlayPause,
                        onLoopClick = viewModel::toggleLoop,
                        onMuteClick = viewModel::toggleMute,
                        onVolumeChange = viewModel::setVolume,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Settings Dialog
        if (showSettings) {
            SettingsDialog(
                currentInterval = transitionInterval,
                availableAudios = audios,
                selectedAudioId = selectedAudioId,
                onIntervalChange = viewModel::setTransitionInterval,
                onAudioSelect = viewModel::selectAudioTrack,
                onDismiss = { showSettings = false }
            )
        }
    }
}
