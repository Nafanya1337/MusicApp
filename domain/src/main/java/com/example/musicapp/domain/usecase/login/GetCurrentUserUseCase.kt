package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.models.login.User
import com.example.musicapp.domain.repository.LoginRepository

class GetCurrentUserUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(callback: (User?) -> Unit) {
        loginRepository.getCurrentUser(callback)
    }
}