package com.example.musicapp.presentation.login.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.login.SignInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val signInUseCase: SignInUseCase): ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.execute(email = email, password = password) { result ->
                this@SignInViewModel.result.postValue(result)
            }
        }
    }
}