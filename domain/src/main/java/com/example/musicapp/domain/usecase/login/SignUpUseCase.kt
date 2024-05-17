package com.example.musicapp.domain.usecase.login

import com.example.musicapp.domain.repository.LoginRepository


class SignUpUseCase (private val loginRepository: LoginRepository) {
    suspend fun execute(email: String, password: String, nickname: String, imageUri: String, callback: (Boolean) -> Unit) {
        loginRepository.signUp(email = email, password = password, nickname = nickname, imageUri = imageUri, callback = callback)
    }
}