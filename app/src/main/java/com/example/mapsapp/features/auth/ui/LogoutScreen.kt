package com.example.mapsapp.features.auth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.auth.LogoutViewModel
import com.example.mapsapp.utils.AuthResult

@Composable
fun LogoutScreen(navigateToHome: () -> Unit) {
    val context = LocalContext.current
    val viewModel: LogoutViewModel = viewModel()

    val loggedOut by viewModel.loggedOut
    val showError by viewModel.showError
    val authResult by viewModel.authResult

    LaunchedEffect(loggedOut) {
        if (loggedOut) navigateToHome()
    }

    if (showError) {
        val errorMessage = (authResult as AuthResult.Error).message
        Toast.makeText(context, errorMessage.ifBlank { "An error has ocurred" }, Toast.LENGTH_LONG).show()
        viewModel.errorMessageShowed()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text("Loging out…")
    }
}