package com.example.musicapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.home.RadioVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val playlistRepositoryImpl: PlaylistRepositoryImpl
) : ViewModel() {

    val chartList = MutableLiveData<TrackListVO>()
    val recommendationsList = MutableLiveData<List<RadioVO>>(emptyList())

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            chartList.postValue(TrackListVO(list = playlistRepositoryImpl.getChart().track.data, title = "Chart"))
            recommendationsList.postValue((playlistRepositoryImpl.getRadio().data))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playlistRepositoryImpl =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).playlistRepositoryImpl

                HomeViewModel(
                    playlistRepositoryImpl
                )
            }
        }
    }

}