package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.TrackRepository

class DeleteFromFavouriteUseCase(private val trackRepository: TrackRepository) {

    fun execute(id: Long){
        trackRepository.deleteFromFavourite(id = id)
    }

}