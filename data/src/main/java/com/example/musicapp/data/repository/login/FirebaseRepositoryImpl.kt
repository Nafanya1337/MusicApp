package com.example.musicapp.data.repository.login


import android.net.Uri
import com.example.musicapp.data.storage.interfaces.FirebaseStorage
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FirebaseRepositoryImpl(private val firebaseStorage: FirebaseStorage) : FirebaseRepository {
    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        imageUri: String,
        callback: (Boolean) -> Unit
    ) {
        firebaseStorage.signUp(
            email = email,
            password = password,
            nickname = nickname,
            imageUri = Uri.parse(imageUri),
            callback = callback
        )
    }

    override suspend fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseStorage.signIn(email = email, password = password, callback = callback)
    }

    override suspend fun getCurrentUser(callback: (User?) -> Unit) {
        val firebaseUser = firebaseStorage.getCurrentUser()
        if (firebaseUser != null) {
            coroutineScope {
                var nickname: String? = null
                var image: String? = null

                val nicknameJob = launch {
                    nickname = firebaseStorage.getUserNickname(firebaseUser.uid)
                }

                val imageJob = launch {
                    image = firebaseStorage.getUserImage(firebaseUser.uid)
                }

                // Ждем завершения всех задач
                nicknameJob.join()
                imageJob.join()

                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    nickname = nickname ?: "",
                    photo = image ?: ""
                )
                callback(user)
            }
        } else {
            callback(null)
        }
    }


    override suspend fun signOut() {
        firebaseStorage.signOut()
    }

    override suspend fun getFavourites(uid: String, callback: (List<TrackVO>) -> Unit) {
        val favourites = firebaseStorage.getUserFavourites(uid)
        callback(favourites)
    }

    override suspend fun addTrackToFavourites(
        uid: String,
        track: TrackVO,
        callback: (Boolean) -> Unit
    ) {
        firebaseStorage.addToFavourites(uid = uid, track = track, callback = callback)
    }

    override suspend fun deleteFromFavourites(
        uid: String,
        trackId: Long,
        callback: (Boolean) -> Unit
    ) {
        firebaseStorage.deleteFromFavourites(uid = uid, trackId = trackId, callback = callback)
    }

}