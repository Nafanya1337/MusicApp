package com.example.musicapp.manager

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.presentation.main.PlayerState
import com.example.musicapp.service.AudioPlayerService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerManager(
    private val context: Context,
    private val serviceClass: Class<AudioPlayerService>
) {
    private var mediaController: MediaController? = null
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var currentPosition = 0

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private var playbackUpdateJob: Job? = null

    fun init() {
        if (mediaController != null) return

        val sessionToken = SessionToken(context, ComponentName(context, serviceClass))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        mediaControllerFuture?.addListener({
            mediaController = mediaControllerFuture?.get()
            observePlayerState()
            startUpdatingPlaybackPosition()

       }, ContextCompat.getMainExecutor(context))

    }

    private fun startUpdatingPlaybackPosition() {
        playbackUpdateJob?.cancel()

        playbackUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                _playbackPosition.value = mediaController?.currentPosition ?: 0L
                delay(500)
            }
        }
    }

    fun pause() {
        mediaController?.pause()
    }

    fun play() {
        mediaController?.play()
    }

    fun release() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
        playbackUpdateJob?.cancel()
        playbackUpdateJob = null
    }

    fun setPlaylist(
        list: List<MediaItem>,
        startPosition: Int = 0,
        autoPlay: Boolean = true
    ) {
        try {
            mediaController?.let { controller ->
                controller.run {
                    setMediaItems(list, startPosition, 0L)
                    if (autoPlay) {
                        prepare()
                        play()
                    }
                }
                currentPosition = startPosition
            } ?: run {
                currentPosition = startPosition
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun observePlayerState() {
        mediaController?.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.update {
                    it.copy(isPLaying = isPlaying)
                }
            }

            @OptIn(UnstableApi::class)
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (mediaItem != null) {
                    _playerState.update {
                        it.copy(mediaItem = mediaItem)
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && mediaController?.mediaMetadata != null) {
                    _playerState.update {
                        it.copy(duration = mediaController!!.duration)
                    }
                }
            }
        })
    }
}