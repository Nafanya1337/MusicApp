package com.example.musicapp.domain.usecase.track

import com.example.musicapp.domain.repository.FirebaseRepository

class DeleteFromFavouriteUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend fun execute(uid: String, trackId: Long, callback: (Boolean) -> Unit){
        firebaseRepository.deleteFromFavourites(uid = uid, trackId = trackId, callback = callback)
    }

}