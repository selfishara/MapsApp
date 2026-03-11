package com.example.mapsapp.features.map.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.core.theme.ThemeViewModel

/**
 * Settings screen of the application.
 *
 * This screen provides basic configuration and account actions,
 * such as logging out from the current session.
 *
 * It also includes the app theme toggle between light and dark mode.
 *
 * @param navController Navigation controller used to move between screens.
 * @param themeViewModel ViewModel responsible for app theme state.
 */
@Composable
fun SettingsScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Settings ⚙️",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Configure your application preferences and manage your account.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Dark mode",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = themeViewModel.isDarkMode.value,
                        onCheckedChange = {
                            themeViewModel.toggleTheme()
                        }
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                /**
                 * Logout button.
                 *
                 * Navigates to the Logout screen which performs
                 * the Supabase signOut operation.
                 */
                Button(
                    onClick = {
                        navController.navigate(Destination.Logout.route) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Log out")
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate(Destination.Profile.route) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Open profile")
                }
            }
        }
    }
}