package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.TrackRepository

class GetTrackInfoUseCase(private val trackRepository: TrackRepository) {

    fun execute(id: Long) {
        trackRepository.getTrackInfo(id = id)
    }

}