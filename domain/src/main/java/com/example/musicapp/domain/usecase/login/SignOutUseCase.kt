package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.LoginRepository

class SignOutUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute() {
        loginRepository.signOut()
    }
}