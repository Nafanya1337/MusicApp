package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.FirebaseRepository

class SignOutUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun execute() {
        firebaseRepository.signOut()
    }
}