package com.example.musicapp.data.repository.login


import android.net.Uri
import com.example.musicapp.data.storage.interfaces.AuthStorage
import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.LoginRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginRepositoryImpl(private val authStorage: AuthStorage) : LoginRepository {
    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        imageUri: String,
        callback: (Boolean) -> Unit
    ) {
        authStorage.signUp(
            email = email,
            password = password,
            nickname = nickname,
            imageUri = Uri.parse(imageUri),
            callback = callback
        )
    }

    override suspend fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
        authStorage.signIn(email = email, password = password, callback = callback)
    }

    override suspend fun getCurrentUser(callback: (User?) -> Unit) {
        val firebaseUser = authStorage.getCurrentUser()
        if (firebaseUser != null) {
            coroutineScope {
                var nickname: String? = null
                var image: String? = null

                val nicknameJob = launch {
                    nickname = authStorage.getUserNickname(firebaseUser.uid)
                }

                val imageJob = launch {
                    image = authStorage.getUserImage(firebaseUser.uid)
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
        authStorage.signOut()
    }
}