package com.example.mapsapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mapsapp.core.layout.MainScaffold
import com.example.mapsapp.features.map.ui.MapScreen
import com.example.mapsapp.features.map.ui.screens.AboutScreen
import com.example.mapsapp.features.map.ui.screens.SettingsScreen
import com.example.mapsapp.features.marker.ui.CreateMarkerScreen

/**
 * Main navigation host of the application.
 *
 * This component defines all navigation routes used in the app and
 * connects them with their corresponding screens.
 *
 * The navigation is wrapped inside [MainScaffold], which provides
 * the main layout structure (drawer, top bar, etc.).
 *
 * Navigation flow:
 *
 * - Maps → Main map screen
 * - Settings → App settings screen
 * - About → Information about the application
 * - CreateMarker → Marker creation screen with coordinates
 *
 * The CreateMarker route receives latitude and longitude as
 * navigation arguments when the user performs a long click on the map.
 *
 * @param navController Controller responsible for handling navigation events.
 */
@Composable
fun AppNavHost(navController: NavHostController) {

    MainScaffold(navController) {

        NavHost(
            navController = navController,
            startDestination = Destination.Maps.route
        ) {

            /**
             * Main map screen.
             */
            composable(Destination.Maps.route) {
                MapScreen(navController)
            }

            /**
             * Settings screen.
             */
            composable(Destination.Settings.route) {
                SettingsScreen()
            }

            /**
             * About screen.
             */
            composable(Destination.About.route) {
                AboutScreen()
            }

            /**
             * Marker creation screen.
             *
             * Receives latitude and longitude from the map
             * when the user performs a long press.
             */
            composable(
                route = Destination.CreateMarker.route,
                arguments = listOf(
                    navArgument("latitude") { type = NavType.StringType },
                    navArgument("longitude") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val latitude =
                    backStackEntry.arguments?.getString("latitude")?.toDoubleOrNull()

                val longitude =
                    backStackEntry.arguments?.getString("longitude")?.toDoubleOrNull()

                if (latitude != null && longitude != null) {

                    CreateMarkerScreen(
                        navController = navController,
                        latitude = latitude,
                        longitude = longitude
                    )

                } else {
                    /**
                     * Fallback in case arguments are missing.
                     * Navigates back to the map to avoid crashes.
                     */
                    navController.popBackStack()
                }
            }
        }
    }
}