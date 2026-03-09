package com.example.mapsapp.features.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.foundation.layout.Arrangement
import com.example.mapsapp.features.auth.LoginViewModel

@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    navigateToRegister: () -> Unit
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

        Button(onClick = { viewModel.signIn() }) {
            Text("Login")
        }

        Button(onClick = navigateToRegister) {
            Text("Register here")
        }
    }
}