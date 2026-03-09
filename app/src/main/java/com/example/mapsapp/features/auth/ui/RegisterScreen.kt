package com.example.mapsapp.features.auth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.auth.RegisterViewModel
import com.example.mapsapp.utils.AuthResult

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

        if (errorMessage.contains("user_already_exists")) {
            Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
        }

        viewModel.errorMessageShowed()
    }

    RegisterForm(viewModel, navigateToLogin)
}

@Composable
fun RegisterForm(
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { viewModel.editEmail(it) },
            label = { Text("Email") }
        )

        TextField(
            value = password,
            onValueChange = { viewModel.editPassword(it) },
            label = { Text("Password") }
        )

        Button(onClick = { viewModel.signUp() }) {
            Text("Register")
        }

        Button(onClick = navigateToLogin) {
            Text("Back to login")
        }
    }
}