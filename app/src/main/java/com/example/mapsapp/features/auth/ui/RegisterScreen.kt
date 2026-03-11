package com.example.mapsapp.features.auth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.auth.RegisterViewModel
import com.example.mapsapp.utils.AuthResult

/**
 * Registration screen.
 *
 * Allows the user to create a new account using email and password.
 *
 * @param onRegisterSuccess Called when registration succeeds.
 * @param navigateToLogin Navigates back to the login screen.
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel()

    val isLoggedIn by viewModel.isLoggedIn
    val authResult by viewModel.authResult
    val showError by viewModel.showError

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onRegisterSuccess()
    }

    if (showError) {
        val errorMessage = (authResult as AuthResult.Error).message

        Toast.makeText(
            context,
            errorMessage.ifBlank { "An error has occurred" },
            Toast.LENGTH_LONG
        ).show()

        viewModel.errorMessageShowed()
    }

    RegisterForm(viewModel, navigateToLogin)
}

/**
 * Registration form UI.
 *
 * Displays email and password input fields and provides
 * actions to register or navigate back to the login screen.
 *
 * @param viewModel ViewModel that manages the register state.
 * @param navigateToLogin Callback used to navigate to the login screen.
 */
@Composable
fun RegisterForm(
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password
    val isLoggedIn by viewModel.isLoggedIn

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { viewModel.editEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { viewModel.editPassword(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isLoggedIn) {
            Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        } else {
            CircularProgressIndicator()
        }

        Button(
            onClick = navigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to login")
        }
    }
}