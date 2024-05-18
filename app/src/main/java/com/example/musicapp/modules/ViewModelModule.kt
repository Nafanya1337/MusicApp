package com.example.musicapp.modules

import com.example.musicapp.presentation.MainActivityViewModel
import com.example.musicapp.presentation.account.AccountViewModel
import com.example.musicapp.presentation.artist.ArtistFragmentViewModel
import com.example.musicapp.presentation.home.HomeViewModel
import com.example.musicapp.presentation.login.signIn.SignInViewModel
import com.example.musicapp.presentation.login.signUp.SignUpViewModel
import com.example.musicapp.presentation.playlist.PlaylistViewModel
import com.example.musicapp.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel {
        AccountViewModel(signOutUseCase = get())
    }

    viewModel {
        ArtistFragmentViewModel(
            getArtistInfoUseCase = get(),
            getArtistAlbumsUseCase = get(),
            getArtistTopTrackUseCase = get()
        )
    }

    viewModel {
        HomeViewModel(playlistRepository = get())
    }

    viewModel {
        SignInViewModel(signInUseCase = get())
    }

    viewModel {
        SignUpViewModel(signUpUseCase = get())
    }

    viewModel {
        PlaylistViewModel(playlistRepository = get())
    }

    viewModel {
        SearchViewModel(
            searchTracksUseCase = get(),
            getSearchHistoryUseCase = get(),
            saveSearchHistoryUseCase = get(),
            clearSearchHistoryUseCase = get()
        )
    }

    viewModel {
        MainActivityViewModel(
            trackRepository = get(),
            getCurrentUserUseCase = get(),
            addTrackToFavouritesUseCase = get(),
            getFavouritesUseCase = get(),
            deleteFromFavouriteUseCase = get()
        )
    }
}