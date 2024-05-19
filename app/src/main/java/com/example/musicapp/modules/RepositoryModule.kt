package com.example.musicapp.modules

import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl
import com.example.musicapp.data.repository.login.FirebaseRepositoryImpl
import com.example.musicapp.data.repository.search.SearchRequestRepositoryImpl
import com.example.musicapp.data.repository.track.TrackRepositoryImpl
import com.example.musicapp.domain.repository.ArtistRepository
import com.example.musicapp.domain.repository.FirebaseRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.SearchRequestRepository
import com.example.musicapp.domain.repository.TrackRepository
import org.koin.dsl.module

val RepositoryModule = module {
    factory<ArtistRepository> {
        ArtistRepositoryImpl(musicApi = get())
    }

    factory<FirebaseRepository> {
        FirebaseRepositoryImpl(firebaseStorage = get())
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(musicApi = get())
    }

    factory<SearchRequestRepository> {
        SearchRequestRepositoryImpl(searchRequestStorage = get(), musicApi = get())
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(musicApi = get())
    }
}