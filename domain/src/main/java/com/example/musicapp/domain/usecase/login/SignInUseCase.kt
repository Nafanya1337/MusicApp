package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.LoginRepository

class SignInUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String, password: String, callback: (Boolean) -> Unit) {
        loginRepository.signIn(email = email, password = password, callback = callback)
    }
}