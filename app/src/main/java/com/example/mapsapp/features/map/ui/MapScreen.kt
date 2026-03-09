package com.example.mapsapp.features.map.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.core.navigation.Destination
import com.example.mapsapp.core.permissions.*

/**
 * Main screen responsible for displaying the map.
 *
 * It manages location permissions, requests marker loading from Supabase,
 * and handles navigation to the marker creation screen.
 *
 * @param navController Navigation controller used for screen navigation.
 * @param viewModel ViewModel responsible for permission and marker state handling.
 */
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = viewModel()
) {
    val context = LocalContext.current

    val permissionManager = rememberPermissionManager(AppPermission.Location)
    val uiState by viewModel.uiState
    val markers by viewModel.markers
    val isLoadingMarkers by viewModel.isLoadingMarkers
    val errorMessage by viewModel.errorMessage

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

    errorMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        viewModel.clearErrorMessage()
    }

    when (uiState) {
        MapPermissionState.NavigateToMap -> {
            MapContent(
                markers = markers,
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