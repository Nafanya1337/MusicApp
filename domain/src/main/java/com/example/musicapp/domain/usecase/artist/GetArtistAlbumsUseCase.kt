package com.example.musicapp.domain.usecase.artist

import com.example.musicapp.domain.models.artist.AlbumVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.domain.repository.ArtistRepository

class GetArtistAlbumsUseCase(private val artistRepository: ArtistRepository) {
    suspend fun execute(id: Long): List<RadioVO> = artistRepository.getArtistAlbums(id = id)
}