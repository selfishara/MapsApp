package com.example.mapsapp.features.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.utils.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for the splash screen flow.
 *
 * It checks whether there is an active authenticated session and exposes
 * the destination that should be opened after the splash screen finishes.
 */
class SplashViewModel : ViewModel() {

    private val authRepository = AuthRepository(MyApp.database)

    private val _startDestination = mutableStateOf<String?>(null)
    val startDestination: State<String?> = _startDestination

    /**
     * Checks the current user session and decides the next screen.
     *
     * If a valid session exists, the app navigates to Maps.
     * Otherwise, it navigates to Login.
     */
    fun checkSession() {
        viewModelScope.launch {
            delay(1200)

            _startDestination.value =
                if (authRepository.isLoggedIn()) {
                    Destination.Maps.route
                } else {
                    Destination.Login.route
                }
        }
    }
}