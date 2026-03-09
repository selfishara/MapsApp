package com.example.mapsapp.features.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.utils.AuthRepository
import com.example.mapsapp.utils.AuthResult
import kotlinx.coroutines.launch

class LogoutViewModel : ViewModel() {

    private val authRepo = AuthRepository(MyApp.database)

    private val _loggedOut = mutableStateOf(false)
    val loggedOut: State<Boolean> = _loggedOut

    private val _showError = mutableStateOf(false)
    val showError: State<Boolean> = _showError

    private val _authResult = mutableStateOf<AuthResult>(AuthResult.Success)
    val authResult: State<AuthResult> = _authResult

    init {
        logout()
    }

    fun logout() {
        viewModelScope.launch {
            val result = authRepo.logout()
            _authResult.value = result

            if (result is AuthResult.Success) {
                _loggedOut.value = true
            } else {
                _showError.value = true
            }
        }
    }

    fun errorMessageShowed() {
        _showError.value = false
    }
}