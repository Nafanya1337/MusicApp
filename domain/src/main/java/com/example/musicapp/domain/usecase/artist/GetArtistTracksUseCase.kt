package com.example.musicapp.domain.usecase.artist

import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.repository.ArtistRepository

class GetArtistTracksUseCase(private val artistRepository: ArtistRepository) {
    suspend fun execute(id: Long): List<TrackVO> = artistRepository.getArtistTracks(id = id)
}