package com.example.musicapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.data.repository.track.TrackRepositoryImpl
import com.example.musicapp.domain.models.CurrentTrackVO
import com.example.musicapp.domain.models.TrackListVO

class MainActivityViewModel(private val trackRepositoryImpl: TrackRepositoryImpl) : ViewModel() {

    val track = MutableLiveData<CurrentTrackVO?>(null)
    val isPlaying = MutableLiveData<Boolean>()
    val currentPlaylist = MutableLiveData<TrackListVO>()
    val currentPosition = MutableLiveData<Int>()
    val nextButtonEnabled = MutableLiveData<Boolean>()
    val prevButtonEnabled = MutableLiveData<Boolean>()
    val isRemeshing = MutableLiveData<Boolean>(false)
    val setOfPositions = mutableSetOf<Int>()

    fun updateButtonStates() {
        val playlist = currentPlaylist.value?.list
        val position = currentPosition.value ?: 0
        nextButtonEnabled.value = position < (playlist?.size?.minus(1) ?: 0)
        prevButtonEnabled.value = position != 0
    }

    suspend fun playTrack(id: Long) {
        try {
            track.postValue(trackRepositoryImpl.streamTrack(id = id))
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

    fun setCurrentTrackList(trackList: TrackListVO){
        currentPlaylist.value = trackList
    }

    fun setCurrentPosition(position: Int){
        currentPosition.value = position
        if (isRemeshing.value == true)
            setOfPositions.add(position)
        updateButtonStates()
    }

    fun nextPosition(){
        if (!isRemeshing.value!!) {
            if (currentPosition.value != currentPlaylist.value?.list?.size?.minus(1))
                currentPosition.value?.plus(1)?.let { this.setCurrentPosition(it) }
            else
                playPause()
        }
        else {

        }
    }

    fun prevPosition() {
        if (currentPosition.value != 0)
            currentPosition.value?.minus(1)?.let { this.setCurrentPosition(it) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackRepositoryImpl =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).trackRepositoryImpl

                MainActivityViewModel(
                    trackRepositoryImpl
                )
            }
        }
    }


}