package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.TrackRepository

class DownloadTrackUseCase(private val trackRepository: TrackRepository) {

    fun execute(url: String) {
        trackRepository.downloadTrack(url = url)
    }

}