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
 * ViewModel responsible for logging the current user out.
 *
 * The logout action is triggered as soon as this ViewModel is created.
 * The UI observes [loggedOut] to navigate back to the login screen.
 */
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

    /**
     * Closes the current authenticated session.
     *
     * On success, [loggedOut] becomes true.
     * On failure, the UI is notified through [showError].
     */
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

    /**
     * Hides the current error message after it has been shown in the UI.
     */
    fun errorMessageShowed() {
        _showError.value = false
    }
}