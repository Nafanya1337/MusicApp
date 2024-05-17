package com.example.musicapp.presentation.login.signUp

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer

import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.domain.usecase.login.SignInUseCase
import com.example.musicapp.domain.usecase.login.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val signUpUseCase: SignUpUseCase): ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun signUp(email: String, password: String, nickname: String, imageUri: String) {
        if (validateData(email)) {

        }
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase.execute(email = email, password = password, nickname = nickname, imageUri = imageUri) { data ->
                result.postValue(data)
            }
        }
    }

    private fun validateData(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val signUpUseCase = SignUpUseCase(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).loginRepositoryImpl
                )
                SignUpViewModel(
                    signUpUseCase
                )
            }
        }

    }

}