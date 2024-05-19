package com.example.musicapp.presentation.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.models.tracks.AlbumInfoVO
import com.example.musicapp.domain.models.tracks.PlaylistType
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(val playlistRepository: PlaylistRepository) : ViewModel() {

    val trackList = MutableLiveData<TrackListVO>()
    val currentPosition = MutableLiveData<Int>()
    lateinit var album: AlbumInfoVO

    fun setAlbumInfo(album: AlbumInfoVO) {
        this.album = album
    }

    fun downloadTrackList(id: Long, title: String, type: PlaylistType) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: List<TrackVO> = playlistRepository.getTracklist( id = id, type)
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