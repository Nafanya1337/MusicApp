package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.FirebaseRepository


class SignUpUseCase (private val firebaseRepository: FirebaseRepository) {
    suspend fun execute(email: String, password: String, nickname: String, imageUri: String, callback: (Boolean) -> Unit) {
        firebaseRepository.signUp(email = email, password = password, nickname = nickname, imageUri = imageUri, callback = callback)
    }
}