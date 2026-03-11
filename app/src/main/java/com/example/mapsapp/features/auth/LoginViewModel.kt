package com.example.mapsapp.features.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.utils.AuthRepository
import com.example.mapsapp.utils.AuthResult
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for the login flow.
 *
 * It stores the login form state, checks whether there is an existing
 * Supabase session and performs the sign-in action.
 */
class LoginViewModel : ViewModel() {

    private val authRepo = AuthRepository(MyApp.database)

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _authResult = mutableStateOf<AuthResult>(AuthResult.Success)
    val authResult: State<AuthResult> = _authResult

    private val _showError = mutableStateOf(false)
    val showError: State<Boolean> = _showError

    fun checkExistingSession() {
        viewModelScope.launch {
            _isLoggedIn.value = authRepo.isLoggedIn()
        }
    }

    fun editEmail(value: String) {
        _email.value = value
    }

    fun editPassword(value: String) {
        _password.value = value
    }

    fun errorMessageShowed() {
        _showError.value = false
    }

    fun signIn() {
        viewModelScope.launch {
            _authResult.value = authRepo.login(
                email = _email.value.trim(),
                password = _password.value
            )

            if (_authResult.value is AuthResult.Error) {
                _showError.value = true
            } else {
                _isLoggedIn.value = authRepo.isLoggedIn()

                if (!_isLoggedIn.value) {
                    _authResult.value = AuthResult.Error("No active session after login.")
                    _showError.value = true
                }
            }
        }
    }
}