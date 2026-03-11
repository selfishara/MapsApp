package com.example.mapsapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mapsapp.core.layout.MainScaffold
import com.example.mapsapp.features.auth.ui.LoginScreen
import com.example.mapsapp.features.auth.ui.LogoutScreen
import com.example.mapsapp.features.auth.ui.RegisterScreen
import com.example.mapsapp.features.map.ui.MapScreen
import com.example.mapsapp.features.map.ui.screens.AboutScreen
import com.example.mapsapp.features.map.ui.screens.SettingsScreen
import com.example.mapsapp.features.marker.ui.CreateMarkerScreen
import com.example.mapsapp.features.marker.ui.MyMarkersScreen
import com.example.mapsapp.features.splash.ui.SplashScreen
import com.example.mapsapp.features.map.ui.screens.ProfileScreen
import com.example.mapsapp.features.marker.ui.EditMarkerScreen

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
 * - Splash → Initial session check screen
 * - Login → User login screen
 * - Register → User registration screen
 * - Logout → User logout screen
 * - Maps → Main map screen
 * - MyMarkers → User markers screen
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
            startDestination = Destination.Splash.route
        ) {

            /**
             * Splash screen.
             *
             * Decides whether the user should go to Login or directly
             * to the main map screen depending on the current session.
             */
            composable(Destination.Splash.route) {
                SplashScreen(
                    navigateToNext = { destination ->
                        navController.navigate(destination) {
                            popUpTo(Destination.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            /**
             * Login screen.
             *
             * Navigates to the map when the user logs in successfully.
             * It also allows navigation to the register screen.
             */
            composable(Destination.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Destination.Maps.route) {
                            popUpTo(Destination.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToRegister = {
                        navController.navigate(Destination.Register.route)
                    }
                )
            }

            /**
             * Register screen.
             *
             * Navigates to the map when the user registers successfully.
             * It also allows returning to the login screen.
             */
            composable(Destination.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Destination.Maps.route) {
                            popUpTo(Destination.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            /**
             * Logout screen.
             *
             * After closing the user session, the app navigates back to login.
             */
            composable(Destination.Logout.route) {
                LogoutScreen(
                    navigateToHome = {
                        navController.navigate(Destination.Login.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            /**
             * Main map screen.
             */
            composable(Destination.Maps.route) {
                MapScreen(navController)
            }

            /**
             * Screen that displays all markers created by the current user.
             */
            composable(Destination.MyMarkers.route) {
                MyMarkersScreen(navController)
            }

            /**
             * Profile screen.
             */
            composable(Destination.Profile.route) {
                ProfileScreen(navController)
            }


            /**
             * Settings screen.
             */
            composable(Destination.Settings.route) {
                SettingsScreen(navController)
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

            /**
             * Marker edition screen.
             *
             * Receives the marker identifier to load and update
             * the selected marker information.
             */
            composable(
                route = Destination.EditMarker.route,
                arguments = listOf(
                    navArgument("markerId") { type = NavType.LongType }
                )
            ) { backStackEntry ->

                val markerId = backStackEntry.arguments?.getLong("markerId")

                if (markerId != null) {
                    EditMarkerScreen(
                        navController = navController,
                        markerId = markerId
                    )
                } else {
                    navController.popBackStack()
                }
            }
        }
    }
}