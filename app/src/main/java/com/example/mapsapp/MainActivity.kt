package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.core.navigation.AppNavHost
import com.example.mapsapp.core.theme.MapsAppTheme
import com.example.mapsapp.core.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()

            MapsAppTheme(
                darkTheme = themeViewModel.isDarkMode.value
            ) {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}