package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.TrackRepository

class AddTrackToFavouritesUseCase(private val trackRepository: TrackRepository) {

    fun execute(id: Long) {
        trackRepository.addToFavourites(id = id)
    }

}