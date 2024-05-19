package com.example.musicapp.domain.repository


import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.home.RadioVO

interface ArtistRepository {
    suspend fun getArtistInfo(id: Long): ArtistVO

    suspend fun getArtistTopTracks(id: Long, limit: Int): List<TrackVO>

    suspend fun getArtistTracks(id: Long): List<TrackVO>

    suspend fun getArtistAlbums(id: Long): List<RadioVO>
}