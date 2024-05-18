package com.example.musicapp.data.storage.interfaces

import android.net.Uri
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.TrackVO
import com.google.firebase.auth.FirebaseUser

interface FirebaseStorage {
    suspend fun signUp(email: String, password: String, nickname: String, imageUri: Uri, callback: (Boolean) -> Unit)
    fun signIn(email: String, password: String, callback: (Boolean) -> Unit)
    fun getCurrentUser() : FirebaseUser?
    suspend fun getUserNickname(uid: String): String
    fun setUserImage(uid: String, imageUri: String)
    suspend fun getUserImage(uid: String): String?
    suspend fun getUserFavourites(uid: String): List<TrackVO>
    fun signOut()
    suspend fun addToFavourites(uid: String, track: TrackVO, callback: (Boolean) -> Unit)

    suspend fun deleteFromFavourites(uid: String, trackId: Long, callback: (Boolean) -> Unit)
}