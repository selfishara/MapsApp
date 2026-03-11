package com.example.mapsapp.features.map.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.core.permissions.*
import com.example.mapsapp.core.theme.ThemeViewModel

@Composable
fun MapScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    viewModel: MapViewModel = viewModel()
) {

    val context = LocalContext.current
    val permissionManager = rememberPermissionManager(AppPermission.Location)

    val uiState by viewModel.uiState
    val errorMessage by viewModel.errorMessage
    val searchQuery by viewModel.searchQuery

    val isDarkTheme = themeViewModel.isDarkMode.value
    val filteredMarkers = viewModel.getFilteredMarkers()

    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        viewModel.onPermissionResult(permissionManager.status)
    }

    LaunchedEffect(uiState) {
        if (uiState == MapPermissionState.NavigateToMap) {
            viewModel.loadMarkers()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (uiState == MapPermissionState.NavigateToMap) {
            viewModel.loadMarkers()
        }
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        viewModel.clearErrorMessage()
    }

    when (uiState) {

        MapPermissionState.NavigateToMap -> {

            Box(modifier = Modifier.fillMaxSize()) {

                MapContent(
                    modifier = Modifier.fillMaxSize(),
                    markers = filteredMarkers,
                    isDarkTheme = isDarkTheme,

                    onCreateMarkerRequest = { latLng ->
                        navController.navigate(
                            Destination.CreateMarker.createRoute(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    },

                    onEditMarkerRequest = { markerId ->
                        navController.navigate(
                            Destination.EditMarker.createRoute(markerId)
                        )
                    }
                )

                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.editSearchQuery(it) },
                    placeholder = { Text("Search markers") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors()
                )
            }
        }

        MapPermissionState.ShowDenied -> {
            PermissionContent(
                status = PermissionStatus.Denied,
                onRetry = permissionManager.requestPermissions
            )
        }

        MapPermissionState.ShowPermanentlyDenied -> {
            PermissionContent(
                status = PermissionStatus.PermanentlyDenied,
                onRetry = {}
            )
        }

        MapPermissionState.Requesting -> {
            PermissionContent(
                status = PermissionStatus.Unknown,
                onRetry = permissionManager.requestPermissions
            )
        }
    }
}