package com.example.musicapp.presentation.main

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.app.MusicApp.Companion.userFav
import com.example.musicapp.domain.models.tracks.CurrentTrackVO
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.TrackRepository
import com.example.musicapp.domain.usecase.login.GetCurrentUserUseCase
import com.example.musicapp.domain.usecase.track.AddTrackToFavouritesUseCase
import com.example.musicapp.domain.usecase.track.DeleteFromFavouriteUseCase
import com.example.musicapp.domain.usecase.track.GetFavouritesUseCase
import com.example.musicapp.manager.PlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val trackRepository: TrackRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val addTrackToFavouritesUseCase: AddTrackToFavouritesUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val deleteFromFavouriteUseCase: DeleteFromFavouriteUseCase,
    private val playerManager: PlayerManager
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _trackList = MutableLiveData<TrackListVO>()

    private val trackMap = mutableMapOf<Long, CurrentTrackVO>()

    private val _currentTrack = MutableLiveData<CurrentTrackVO>()
    val currentTrack: LiveData<CurrentTrackVO> = _currentTrack

    private val _playbackPosition = MutableLiveData<Int>()
    val playbackPosition: LiveData<Int> = _playbackPosition

    init {
        viewModelScope.launch {
            playerManager.playerState.collect { state ->
                if (state.mediaItem != null) {
                    _state.value = state
                    if (currentTrack.value == null || currentTrack.value?.id != state.mediaItem.mediaId.toLong()) {
                        streamCurrentTrack(state.mediaItem.mediaId.toLong())
                    }
                }
            }
        }

        viewModelScope.launch {
            playerManager.playbackPosition.collect { position ->
                val pos = (position / 1000).toInt()
                _playbackPosition.value = pos
            }
        }
    }

    private fun streamCurrentTrack(id: Long) {
        viewModelScope.launch {
            if (!trackMap.containsKey(id)) {
                val track = trackRepository.streamTrack(id)
                track?.let {
                    trackMap.put(
                        id,
                        it.copy(
                            contributors = it.contributors.distinctBy { it.id },
                            duration = (_state.value!!.duration / 1000).toInt()
                        )
                    )
                }
            }
            _currentTrack.value = trackMap[id]
        }
    }

    fun changePlayPauseState() = when (_state.value?.isPLaying) {
        true -> playerManager.pause()
        false -> playerManager.play()
        null -> {}
    }

    fun setPlayList(tracks: TrackListVO, positionOfCurrentTrack: Int) {
        try {
            loadPlaylistToPlayerManager(tracks, positionOfCurrentTrack)
            trackMap.clear()
            setCurrentTrackList(tracks)
        } catch (e: Exception) {

        }
    }

    fun loadPlaylistToPlayerManager(tracks: TrackListVO, positionOfCurrentTrack: Int) {
        viewModelScope.launch {
            val mediaItems = tracks.list.map { (it as TrackVO).toMediaItem() }
            playerManager.setPlaylist(mediaItems, positionOfCurrentTrack)
        }
    }

    fun initUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase.execute { data ->
                this@MainActivityViewModel._user.postValue(data)
            }
        }
    }

    fun initFav() {
        viewModelScope.launch(Dispatchers.IO) {
            userFav.postValue(_user.value?.id?.let {
                getFavouritesUseCase.execute(it)
                    .toMutableList()
            })
        }
    }

    private fun setCurrentTrackList(trackList: TrackListVO) {
        _trackList.value = trackList
    }

    fun CurrentTrackVO.toTrackVO(): TrackVO {
        return TrackVO(
            id = this.id,
            title = this.title,
            explicitLyrics = this.explicitLyrics,
            preview = this.preview,
            artist = ArtistVO(
                id = this.contributors[0].id,
                name = this.contributors[0].name,
                share = null,
                picture = this.contributors[0].picture
            ),
            album = this.album,
            position = null,
            duration = duration
        )
    }
}

@OptIn(UnstableApi::class)
fun TrackVO.toMediaItem() = MediaItem.Builder()
    .setMediaId(id.toString())
    .setUri(preview)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(this.artist.name)
            .setArtworkUri(album?.picture?.toUri())
            .setDurationMs((duration * 1000).toLong())
            .build()
    )
    .build()