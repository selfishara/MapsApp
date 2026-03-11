package com.example.mapsapp.features.auth.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mapsapp.features.auth.LoginViewModel

/**
 * Login form UI.
 *
 * Displays email and password input fields and provides
 * actions to log in or navigate to the registration screen.
 *
 * @param viewModel ViewModel that manages the login state.
 * @param navigateToRegister Callback used to navigate to the register screen.
 */
@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    navigateToRegister: () -> Unit
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
                onClick = { viewModel.signIn() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        } else {
            CircularProgressIndicator()
        }

        Button(
            onClick = navigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register here")
        }
    }
}