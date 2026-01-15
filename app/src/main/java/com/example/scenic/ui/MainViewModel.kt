package com.example.scenic.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.scenic.data.model.Audio
import com.example.scenic.data.model.Image
import com.example.scenic.data.repository.VibesRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import com.example.scenic.data.local.AppDatabase

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = VibesRepository(database.vibesDao())
    private var exoPlayer: ExoPlayer? = null

    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images: StateFlow<List<Image>> = _images.asStateFlow()

    private val _audios = MutableStateFlow<List<Audio>>(emptyList())
    val audios: StateFlow<List<Audio>> = _audios.asStateFlow()

    // Player State
    private val _currentImageIndex = MutableStateFlow(0)
    val currentImageIndex: StateFlow<Int> = _currentImageIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _volume = MutableStateFlow(0.5f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Settings
    private val _transitionInterval = MutableStateFlow(10000L) // milliseconds
    val transitionInterval: StateFlow<Long> = _transitionInterval.asStateFlow()

    private val _selectedAudioId = MutableStateFlow<Int?>(null)
    val selectedAudioId: StateFlow<Int?> = _selectedAudioId.asStateFlow()


    init {
        initializePlayer()
        loadData()
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(getApplication()).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            volume = _volume.value
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
                
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        _isPlaying.value = false
                    }
                }
            })
        }
    }

    fun nextImage() {
        val currentList = _images.value
        if (currentList.isNotEmpty()) {
            _currentImageIndex.value = (_currentImageIndex.value + 1) % currentList.size
        }
    }

    fun previousImage() {
        val currentList = _images.value
        if (currentList.isNotEmpty()) {
            val nextIndex = _currentImageIndex.value - 1
            _currentImageIndex.value = if (nextIndex < 0) currentList.size - 1 else nextIndex
        }
    }

    fun setImageIndex(index: Int) {
        val currentList = _images.value
        if (currentList.isNotEmpty() && index in currentList.indices) {
            _currentImageIndex.value = index
        }
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun toggleLoop() {
        val player = exoPlayer ?: return
        val newLoopingState = !_isLooping.value
        _isLooping.value = newLoopingState
        player.repeatMode = if (newLoopingState) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }

    fun toggleMute() {
        val player = exoPlayer ?: return
        val newMutedState = !_isMuted.value
        _isMuted.value = newMutedState
        player.volume = if (newMutedState) 0f else _volume.value
    }

    fun setVolume(newVolume: Float) {
        val player = exoPlayer ?: return
        val clampedVolume = newVolume.coerceIn(0f, 1f)
        _volume.value = clampedVolume
        if (!_isMuted.value) {
            player.volume = clampedVolume
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Run both calls in parallel
                val imagesDeferred = async { repository.getImages() }
                val audiosDeferred = async { repository.getAudios() }

                val imagesResult = imagesDeferred.await()
                val audiosResult = audiosDeferred.await()

                imagesResult.onSuccess { 
                    _images.value = it 
                }.onFailure { 
                    _errorMessage.value = "Failed to load images: ${it.message}"
                }

                audiosResult.onSuccess { 
                    _audios.value = it
                    if (it.isNotEmpty()) {
                        prepareAudio(it.first())
                    }
                }.onFailure { 
                     val error = "Failed to load audios: ${it.message}"
                     if (_errorMessage.value == null) {
                        _errorMessage.value = error
                     } else {
                        _errorMessage.value += "\n$error"
                     }
                }
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry() {
        loadData()
    }

    private fun prepareAudio(audio: Audio) {
        val player = exoPlayer ?: return
        val mediaItem = MediaItem.fromUri(audio.url)
        player.setMediaItem(mediaItem)
        player.prepare()
        // Don't auto-play by default, let user choose
    }

    fun setTransitionInterval(intervalMs: Long) {
        _transitionInterval.value = intervalMs
    }

    fun selectAudioTrack(audioId: Int) {
        val audio = _audios.value.find { it.id == audioId }
        if (audio != null) {
            _selectedAudioId.value = audioId
            prepareAudio(audio)
        }
    }


    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}
