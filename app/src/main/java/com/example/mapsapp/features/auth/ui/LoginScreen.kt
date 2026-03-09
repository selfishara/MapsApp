package com.example.mapsapp.features.auth.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.auth.LoginViewModel
import com.example.mapsapp.utils.AuthResult

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

    LaunchedEffect(Unit) {
        viewModel.checkExistingSession()
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onLoginSuccess()
    }

    if (showError) {
        val errorMessage = (authResult as AuthResult.Error).message

        if (errorMessage.contains("invalid_credentials")) {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
        }

        viewModel.errorMessageShowed()
    }

    LoginForm(viewModel, navigateToRegister)
}