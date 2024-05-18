package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.FirebaseRepository

class SignInUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun execute(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseRepository.signIn(email = email, password = password, callback = callback)
    }
}