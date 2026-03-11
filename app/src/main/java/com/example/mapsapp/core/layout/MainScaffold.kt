package com.example.mapsapp.core.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.mapsapp.core.components.DrawerMenu
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.features.profile.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val profileViewModel: ProfileViewModel = viewModel()
    val avatarUrl by profileViewModel.avatarUrl

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val authRoutes = setOf(
        Destination.Splash.route,
        Destination.Login.route,
        Destination.Register.route,
        Destination.Logout.route
    )

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

                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

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
                            text = "YourPoint",
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

                        IconButton(
                            onClick = {
                                navController.navigate(Destination.Profile.route) {
                                    launchSingleTop = true
                                }
                            }
                        ) {

                            if (!avatarUrl.isNullOrBlank()) {

                                AsyncImage(
                                    model = avatarUrl,
                                    contentDescription = "Profile avatar",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            } else {

                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile"
                                )
                            }
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