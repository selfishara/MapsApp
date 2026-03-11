package com.example.mapsapp.core.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mapsapp.core.components.DrawerMenu
import com.example.mapsapp.core.navigation.Destination
import kotlinx.coroutines.launch

/**
 * Main scaffold of the application.
 *
 * This composable provides the shared app layout:
 * - top app bar
 * - drawer menu
 * - content container
 *
 * Authentication screens are excluded from this scaffold so the user
 * cannot access the drawer or navigate into protected screens before
 * logging in.
 *
 * @param navController Navigation controller used by the app.
 * @param content Main screen content rendered inside the scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    // estado del drawer (abierto/cerrado)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // scope para lanzar corrutinas (obligatorio para abrir/cerrar el drawer)
    val scope = rememberCoroutineScope()

    // ruta actual (para marcar el item seleccionado en el drawer)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    /**
     * Routes where the scaffold should not be shown.
     *
     * These screens belong to the authentication flow and must not display
     * the drawer or the main top bar.
     */
    val authRoutes = setOf(
        Destination.Splash.route,
        Destination.Login.route,
        Destination.Register.route,
        Destination.Logout.route
    )

    /**
     * If the current destination belongs to the authentication flow,
     * only the content is rendered without scaffold or drawer.
     */
    if (currentRoute in authRoutes) {
        content()
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            DrawerMenu(
                currentRoute = currentRoute,
                onNavigate = { destination ->

                    /**
                     * Special handling for the Maps destination.
                     *
                     * If Maps already exists in the back stack, return to it.
                     * Otherwise, navigate to it normally.
                     */
                    if (destination == Destination.Maps) {
                        val popped = navController.popBackStack(
                            Destination.Maps.route,
                            inclusive = false
                        )

                        if (!popped) {
                            navController.navigate(Destination.Maps.route) {
                                launchSingleTop = true
                            }
                        }
                    } else {
                        /**
                         * Standard navigation for the rest of the drawer screens.
                         *
                         * Duplicated destinations are avoided and previous state
                         * is restored when possible.
                         */
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    // corrutina para cerrar drawer tras navegar
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "📍 PinPoint",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        /**
                         * User/profile icon shown in the top bar.
                         *
                         * It navigates to the Profile screen.
                         */
                        IconButton(
                            onClick = {
                                navController.navigate(Destination.Profile.route) {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                content()
            }
        }
    }
}