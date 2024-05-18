package com.example.musicapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    val chartList = MutableLiveData<TrackListVO>()
    val recommendationsList = MutableLiveData<List<RadioVO>>(emptyList())

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            chartList.postValue(TrackListVO(list = playlistRepository.getChart().track.data, title = "Chart"))
            recommendationsList.postValue((playlistRepository.getRadio().data))
        }
    }
}