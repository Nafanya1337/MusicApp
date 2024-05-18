package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.FirebaseRepository

class GetCurrentUserUseCase(private val firebaseRepository: FirebaseRepository) {
    suspend fun execute(callback: (User?) -> Unit) {
        firebaseRepository.getCurrentUser(callback)
    }
}