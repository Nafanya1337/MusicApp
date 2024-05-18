package com.example.musicapp.modules

import com.example.musicapp.domain.usecase.artist.GetArtistAlbumsUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistInfoUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistTopTrackUseCase
import com.example.musicapp.domain.usecase.artist.GetArtistTracksUseCase
import com.example.musicapp.domain.usecase.library.GetUserFavouritesUseCase
import com.example.musicapp.domain.usecase.login.GetCurrentUserUseCase
import com.example.musicapp.domain.usecase.login.SignInUseCase
import com.example.musicapp.domain.usecase.login.SignOutUseCase
import com.example.musicapp.domain.usecase.login.SignUpUseCase
import com.example.musicapp.domain.usecase.search.ClearSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.GetSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.SaveSearchRequestUseCase
import com.example.musicapp.domain.usecase.search.SearchTracksUseCase
import com.example.musicapp.domain.usecase.track.AddTrackToFavouritesUseCase
import com.example.musicapp.domain.usecase.track.DeleteFromFavouriteUseCase
import com.example.musicapp.domain.usecase.track.DownloadTrackUseCase
import com.example.musicapp.domain.usecase.track.GetFavouritesUseCase
import com.example.musicapp.domain.usecase.track.GetTrackInfoUseCase
import org.koin.dsl.module

val UseCaseModule = module {
    factory {
        GetArtistInfoUseCase(artistRepository = get())
    }
    factory {
        GetArtistAlbumsUseCase(artistRepository = get())
    }
    factory {
        GetArtistTopTrackUseCase(artistRepository = get())
    }
    factory {
        GetArtistTracksUseCase(artistRepository = get())
    }
    factory {
        GetUserFavouritesUseCase(firebaseRepository = get())
    }
    factory {
        GetCurrentUserUseCase(firebaseRepository = get())
    }
    factory {
        SignInUseCase(firebaseRepository = get())
    }
    factory {
        SignOutUseCase(firebaseRepository = get())
    }
    factory {
        SignUpUseCase (firebaseRepository = get())
    }
    factory {
        ClearSearchHistoryUseCase(searchRequestRepository = get())
    }
    factory {
        GetSearchHistoryUseCase(searchRequestRepository = get())
    }
    factory {
        SaveSearchRequestUseCase(searchRequestRepository = get())
    }
    factory {
        AddTrackToFavouritesUseCase(firebaseRepository = get())
    }
    factory {
        DeleteFromFavouriteUseCase(firebaseRepository = get())
    }
    factory {
        DownloadTrackUseCase(trackRepository = get())
    }
    factory {
        GetFavouritesUseCase(firebaseRepository = get())
    }
    factory {
        GetTrackInfoUseCase(trackRepository = get())
    }
    factory {
        SearchTracksUseCase(searchRequestRepository = get())
    }
}