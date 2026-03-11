package com.example.mapsapp.features.splash.ui

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.splash.SplashViewModel

/**
 * Splash screen shown when the application starts.
 *
 * This screen checks whether there is an active session and redirects
 * the user either to the login screen or to the main map screen.
 *
 * @param navigateToNext Callback used to navigate to the next destination.
 */
@Composable
fun SplashScreen(
    navigateToNext: (String) -> Unit
) {
    val viewModel: SplashViewModel = viewModel()
    val startDestination by viewModel.startDestination

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(startDestination) {
        startDestination?.let { destination ->
            navigateToNext(destination)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("MapsApp")
        CircularProgressIndicator()
    }
}