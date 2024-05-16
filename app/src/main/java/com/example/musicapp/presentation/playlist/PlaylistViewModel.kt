package com.example.musicapp.presentation.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.domain.models.AlbumInfoVO
import com.example.musicapp.domain.models.PlaylistType
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.TrackVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(val playlistRepositoryImpl: PlaylistRepositoryImpl) : ViewModel() {

    val trackList = MutableLiveData<TrackListVO>()
    val currentPosition = MutableLiveData<Int>()
    lateinit var album: AlbumInfoVO

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playlistRepositoryImpl =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).playlistRepositoryImpl

                PlaylistViewModel(
                    playlistRepositoryImpl
                )
            }
        }
    }

    fun setAlbumInfo(album: AlbumInfoVO) {
        this.album = album
    }

    fun downloadTrackList(id: Long, title: String, type: PlaylistType) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: List<TrackVO> = playlistRepositoryImpl.getTracklist( id = id, type)
            list.forEach { if (it.album == null) it.album = album }
            trackList.postValue(
                TrackListVO(
                    list = list, title = title
                )
            )
        }
    }

    fun setCurrentPosition(position: Int) {
        currentPosition.value = position
    }

}