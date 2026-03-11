package com.example.mapsapp.features.map.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.core.permissions.AppPermission
import com.example.mapsapp.core.permissions.PermissionContent
import com.example.mapsapp.core.permissions.PermissionStatus
import com.example.mapsapp.core.permissions.rememberPermissionManager
import com.example.mapsapp.core.theme.ThemeViewModel

/**
 * Main screen responsible for displaying the map.
 *
 * It manages location permissions, requests marker loading from Supabase,
 * handles marker filtering by name, refreshes marker data when the screen
 * returns to the foreground, and navigates to the marker creation screen.
 *
 * @param navController Navigation controller used for screen navigation.
 * @param themeViewModel ViewModel responsible for the current light/dark theme state.
 * @param viewModel ViewModel responsible for permission, marker and search state handling.
 */
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

    /**
     * Current manual app theme state used to adapt the Google Map style.
     */
    val isDarkTheme = themeViewModel.isDarkMode.value

    /**
     * Filtered markers displayed on the map according to the current search query.
     */
    val filteredMarkers = viewModel.getFilteredMarkers()

    /**
     * Requests location permission if needed and updates the permission UI state.
     */
    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        viewModel.onPermissionResult(permissionManager.status)
    }

    /**
     * Loads markers the first time the map becomes available.
     */
    LaunchedEffect(uiState) {
        if (uiState == MapPermissionState.NavigateToMap) {
            viewModel.loadMarkers()
        }
    }

    /**
     * Reloads markers every time the map screen returns to the foreground.
     *
     * This ensures that newly created, updated or deleted markers
     * are displayed without needing to restart the screen.
     */
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (uiState == MapPermissionState.NavigateToMap) {
            viewModel.loadMarkers()
        }
    }

    errorMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.clearErrorMessage()
    }

    when (uiState) {
        MapPermissionState.NavigateToMap -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                /**
                 * Search field used to filter markers by their title.
                 */
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.editSearchQuery(it) },
                    label = { Text("Search markers by name") },
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

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
                    }
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