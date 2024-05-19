package com.example.musicapp.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.login.SignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(private val signOutUseCase: SignOutUseCase) : ViewModel() {
    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.execute()
        }
    }
}