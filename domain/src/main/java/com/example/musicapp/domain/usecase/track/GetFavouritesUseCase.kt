package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.repository.FirebaseRepository

class GetFavouritesUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun execute(uid: String): List<TrackVO> {
        val list = mutableListOf<TrackVO>()
        firebaseRepository.getFavourites(uid = uid) {
            list += it
        }
        return list
    }
}