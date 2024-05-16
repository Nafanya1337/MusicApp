package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.TrackRepository

class DeleteTrackUseCase(private val trackRepository: TrackRepository) {

    fun execute(id: Long){
        trackRepository.deleteTrack(id = id)
    }

}