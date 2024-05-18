package com.example.musicapp.domain.usecase.library

import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.repository.FirebaseRepository

class GetUserFavouritesUseCase(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun execute(uid: String, callback: (List<Playlistable>) -> Unit) = firebaseRepository.getFavourites(uid, callback)
}