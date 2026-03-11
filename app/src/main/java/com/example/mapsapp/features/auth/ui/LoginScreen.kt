package com.example.mapsapp.features.auth.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.auth.LoginViewModel
import com.example.mapsapp.utils.AuthResult

/**
 * Login screen responsible for:
 *
 * - Checking if a session already exists
 * - Showing the login form
 * - Displaying authentication errors
 * - Navigating to the map after successful login
 *
 * @param onLoginSuccess Called when the user logs in successfully.
 * @param navigateToRegister Navigates to the register screen.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()

    val isLoggedIn by viewModel.isLoggedIn
    val authResult by viewModel.authResult
    val showError by viewModel.showError

    /**
     * Checks if the user already has an active session.
     */
    LaunchedEffect(Unit) {
        viewModel.checkExistingSession()
    }

    /**
     * Navigates to the main map screen when login succeeds.
     */
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onLoginSuccess()
    }

    /**
     * Displays authentication errors.
     */
    if (showError) {
        val errorMessage = (authResult as AuthResult.Error).message

        if (errorMessage.contains("invalid_credentials")) {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show()
        }

        viewModel.errorMessageShowed()
    }

    LoginForm(viewModel, navigateToRegister)
}