package com.example.musicapp.domain.usecase.artist

import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.repository.ArtistRepository

class GetArtistInfoUseCase(private val artistRepository: ArtistRepository) {
    suspend fun execute(id: Long): ArtistVO = artistRepository.getArtistInfo(id = id)
}