package com.example.musicapp.presentation.login.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.login.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUpUseCase: SignUpUseCase): ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun signUp(email: String, password: String, nickname: String, imageUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase.execute(email = email, password = password, nickname = nickname, imageUri = imageUri) { data ->
                result.postValue(data)
            }
        }
    }
}