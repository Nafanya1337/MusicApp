package com.example.musicapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.MusicApp.Companion.userFav
import com.example.musicapp.domain.models.CurrentTrackVO
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.TrackRepository
import com.example.musicapp.domain.usecase.login.GetCurrentUserUseCase
import com.example.musicapp.domain.usecase.track.AddTrackToFavouritesUseCase
import com.example.musicapp.domain.usecase.track.DeleteFromFavouriteUseCase
import com.example.musicapp.domain.usecase.track.GetFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val trackRepository: TrackRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val addTrackToFavouritesUseCase: AddTrackToFavouritesUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val deleteFromFavouriteUseCase: DeleteFromFavouriteUseCase
) : ViewModel() {

    val track = MutableLiveData<CurrentTrackVO?>(null)
    val isPlaying = MutableLiveData<Boolean>()
    val currentPlaylist = MutableLiveData<TrackListVO>()
    val currentPosition = MutableLiveData<Int>()
    val nextButtonEnabled = MutableLiveData<Boolean>()
    val prevButtonEnabled = MutableLiveData<Boolean>()
    val looping = MutableLiveData<LoopinType>(LoopinType.NONE)
    val user = MutableLiveData<User?>()


    fun updateButtonStates() {
        val playlist = currentPlaylist.value?.list
        val position = currentPosition.value ?: 0
        nextButtonEnabled.value = position < (playlist?.size?.minus(1) ?: 0)
        prevButtonEnabled.value = position != 0
    }

    fun addToFav(callback: (Boolean) -> Unit) {
        val uid = user.value?.id
        val track = track.value?.toTrackVO()
        if (uid != null && track != null) {
            viewModelScope.launch(Dispatchers.IO) {
                addTrackToFavouritesUseCase.execute(uid = uid, track = track) {
                    if (it) {
                        getFav()
                    }
                    callback(it)
                }
            }
        }
    }

    fun changeLoopingType() {
        looping.value?.let {
            this.looping.value = it.nextLoopType()
        }
    }

    fun deleteFromFav(callback: (Boolean) -> Unit) {
        val uid = user.value?.id
        val track = track.value?.toTrackVO()
        if (uid != null && track != null) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteFromFavouriteUseCase.execute(uid = uid, trackId = track.id) {
                    if (it) {
                        getFav()
                    }
                    callback(it)
                }
            }
        }
    }

    suspend fun playTrack(id: Long) {
        try {
            track.postValue(trackRepository.streamTrack(id = id))
            if (track != null) {
                isPlaying.postValue(true)
            }
        } catch (e: Exception) {
            track.postValue(null)
            isPlaying.postValue(false)
        }
    }

    fun playPause() {
        isPlaying.value = !isPlaying.value!!
    }

    fun setCurrentTrackList(trackList: TrackListVO) {
        currentPlaylist.value = trackList
    }

    fun setCurrentPosition(position: Int) {
        currentPosition.value = position
        updateButtonStates()
    }

    fun autoNextPosition() {
        when (looping.value) {
            LoopinType.NONE -> nextPositionByClick()
            LoopinType.TRACK -> setCurrentPosition(currentPosition.value!!)
            LoopinType.PLAYLIST -> setCurrentPosition(0)
            null -> {}
        }
    }

    fun nextPositionByClick() {
        if (currentPosition.value != currentPlaylist.value?.list?.size?.minus(1))
            currentPosition.value?.plus(1)?.let { this.setCurrentPosition(it) }
        else
            playPause()
    }

    fun prevPosition() {
        if (currentPosition.value != 0)
            currentPosition.value?.minus(1)?.let { this.setCurrentPosition(it) }
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase.execute { data ->
                this@MainActivityViewModel.user.postValue(data)
            }
        }
    }

    fun getFav() {
        viewModelScope.launch(Dispatchers.IO) {
            userFav.postValue(user.value?.id?.let {
                getFavouritesUseCase.execute(it)
                    .toMutableList()
            })
        }
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
            position = null
        )
    }

    enum class LoopinType {
        NONE,
        TRACK,
        PLAYLIST;

        fun nextLoopType(): LoopinType {
            return when (this) {
                NONE -> PLAYLIST
                PLAYLIST -> TRACK
                TRACK -> NONE
            }
        }
    }
}