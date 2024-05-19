package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.login.User


interface FirebaseRepository {
    suspend fun signUp(email: String, password: String, nickname: String, imageUri: String, callback: (Boolean) -> Unit)
    suspend fun signIn(email: String, password: String, callback: (Boolean) -> Unit)
    suspend fun getCurrentUser(callback: (User?) -> Unit)
    suspend fun signOut()
    suspend fun getFavourites(uid: String, callback: (List<TrackVO>) -> Unit)

    suspend fun addTrackToFavourites(uid: String, track: TrackVO, callback: (Boolean) -> Unit)

    suspend fun deleteFromFavourites(uid: String, trackId: Long, callback: (Boolean) -> Unit)
}