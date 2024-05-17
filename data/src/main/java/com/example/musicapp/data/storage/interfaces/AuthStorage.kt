package com.example.musicapp.data.storage.interfaces

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface AuthStorage {
    suspend fun signUp(email: String, password: String, nickname: String, imageUri: Uri, callback: (Boolean) -> Unit)
    fun signIn(email: String, password: String, callback: (Boolean) -> Unit)
    fun getCurrentUser() : FirebaseUser?
    suspend fun getUserNickname(uid: String): String
    fun setUserImage(uid: String, imageUri: String)
    suspend fun getUserImage(uid: String): String?
    fun signOut()
}