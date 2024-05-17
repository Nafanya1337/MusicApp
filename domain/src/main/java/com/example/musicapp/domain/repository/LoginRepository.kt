package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.login.User


interface LoginRepository {
    suspend fun signUp(email: String, password: String, nickname: String, imageUri: String, callback: (Boolean) -> Unit)
    suspend fun signIn(email: String, password: String, callback: (Boolean) -> Unit)
    suspend fun getCurrentUser(callback: (User?) -> Unit)
    suspend fun signOut()

}