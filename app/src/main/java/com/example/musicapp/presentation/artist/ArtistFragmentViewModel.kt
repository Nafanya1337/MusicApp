package com.example.musicapp.presentation.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.domain.usecase.artist.GetArtistAlbumsUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistInfoUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistTopTrackUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistFragmentViewModel(
    private val getArtistInfoUseCase: GetArtistInfoUseCase,
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
    private val getArtistTopTrackUseCase: GetArtistTopTrackUseCase
) : ViewModel() {

    val artist = MutableLiveData<ArtistVO>()
    val albums = MutableLiveData<List<RadioVO>>()
    val tracks = MutableLiveData<List<TrackVO>>()


    fun getArtistInfo(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
           artist.postValue(getArtistInfoUseCase.execute(id = id))
        }
    }

    fun getArtistAlbums(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            albums.postValue(getArtistAlbumsUseCase.execute(id = id))
        }
    }

    fun getArtistTopTrack(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracks.postValue(getArtistTopTrackUseCase.execute(id = id))
        }

    }
}