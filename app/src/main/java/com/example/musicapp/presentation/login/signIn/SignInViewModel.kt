package com.example.musicapp.presentation.login.signIn

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.data.repository.login.LoginRepositoryImpl
import com.example.musicapp.domain.usecase.login.SignInUseCase
import com.example.musicapp.presentation.playlist.PlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val signInUseCase: SignInUseCase): ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun signIn(email: String, password: String) {
        if (validateData(email)) {

        }
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.execute(email = email, password = password) { result ->
                this@SignInViewModel.result.postValue(result)
            }
        }
    }

    private fun validateData(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val signInUseCase = SignInUseCase(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).loginRepositoryImpl
                )
                SignInViewModel(
                    signInUseCase
                )
            }
        }

    }

}