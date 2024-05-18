package com.example.musicapp.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.domain.usecase.login.SignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(private val signOutUseCase: SignOutUseCase) : ViewModel() {

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.execute()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val signOutUseCase =
                    SignOutUseCase((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApp).firebaseRepositoryImpl)
                AccountViewModel(
                    signOutUseCase
                )
            }
        }
    }

}