package com.example.musicapp.presentation.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.artist.AlbumVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.domain.usecase.artist.GetArtistAlbumsUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistInfoUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistTopTrackUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistTracksUseCase
import com.example.musicapp.presentation.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistFragmentViewModel(
    private val artistRepositoryImpl: ArtistRepositoryImpl
) : ViewModel() {

    private val getArtistInfoUseCase by lazy {
        GetArtistInfoUseCase(artistRepositoryImpl)
    }

    private val getArtistAlbumsUseCase by lazy {
        GetArtistAlbumsUseCase(artistRepositoryImpl)
    }

    private val getArtistTopTrackUseCase by lazy {
        GetArtistTopTrackUseCase(artistRepositoryImpl)
    }

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val artistRepositoryImpl =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).artistRepositoryImpl

                ArtistFragmentViewModel(
                    artistRepositoryImpl
                )
            }
        }
    }
}