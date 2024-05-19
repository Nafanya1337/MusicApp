package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.repository.FirebaseRepository

class AddTrackToFavouritesUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend fun execute(uid: String, track: TrackVO, callback: (Boolean) -> Unit) {
        firebaseRepository.addTrackToFavourites(uid = uid, track = track, callback = callback)
    }

}