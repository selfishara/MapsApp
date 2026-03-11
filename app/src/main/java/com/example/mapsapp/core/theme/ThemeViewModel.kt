package com.example.mapsapp.core.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * ViewModel responsible for managing the current app theme.
 *
 * It stores whether dark mode is enabled and exposes a toggle action
 * used by the settings screen.
 */
class ThemeViewModel : ViewModel() {

    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode = _isDarkMode

    /**
     * Toggles the app theme between light and dark mode.
     */
    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }
}