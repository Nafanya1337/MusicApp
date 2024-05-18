package com.example.musicapp.presentation.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.usecase.library.GetUserFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryFragmentViewModel(
    private val getUserFavouritesUseCase: GetUserFavouritesUseCase
): ViewModel() {


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val getUserFavouritesUseCase =
                    GetUserFavouritesUseCase(firebaseRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).firebaseRepositoryImpl)

                LibraryFragmentViewModel(

                    getUserFavouritesUseCase
                )
            }
        }
    }

}