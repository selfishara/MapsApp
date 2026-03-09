package com.example.mapsapp.features.marker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.core.permissions.AppPermission
import com.example.mapsapp.core.permissions.PermissionContent
import com.example.mapsapp.core.permissions.PermissionStatus
import com.example.mapsapp.core.permissions.rememberPermissionManager
import com.example.mapsapp.features.marker.CreateMarkerPermissionState
import com.example.mapsapp.features.marker.CreateMarkerViewModel
import androidx.navigation.NavController

/**
 * Screen responsible for creating a new map marker.
 *
 * It manages camera permissions and displays the marker creation UI.
 * Coordinates are received from the map screen when the user performs
 * a long click on the map.
 *
 * @param navController Navigation controller used to return to the map.
 * @param latitude Latitude where the marker will be created.
 * @param longitude Longitude where the marker will be created.
 * @param viewModel ViewModel managing marker creation logic.
 */
@Composable
fun CreateMarkerScreen(
    navController: NavController, //para poder navegar de vuelta al mapa después de crear el marcador
    latitude: Double,
    longitude: Double,
    viewModel: CreateMarkerViewModel = viewModel()
) {
    val permissionManager = rememberPermissionManager(AppPermission.CameraAndAudio)
    val uiState by viewModel.uiState
    val creationSuccess by viewModel.creationSuccess

    /**
     * Set marker coordinates when entering the screen.
     */
    LaunchedEffect(Unit) {
        viewModel.setCoordinates(latitude, longitude)
    }

    /**
     * If marker creation succeeds, return to the map screen.
     */
    if (creationSuccess) {
        //para evitar recomposición extra
        LaunchedEffect(Unit) {
            navController.popBackStack()
            viewModel.consumeCreationSuccess()
        }
    }

    /**
     * Camera permission handling.
     */
    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        viewModel.onPermissionResult(permissionManager.status)
    }

    when (uiState) {
        CreateMarkerPermissionState.NavigateToCreateMarker -> {
            CreateMarkerContent(viewModel)
        }

        CreateMarkerPermissionState.ShowDenied -> {
            PermissionContent(
                status = PermissionStatus.Denied,
                onRetry = permissionManager.requestPermissions
            )
        }

        CreateMarkerPermissionState.ShowPermanentlyDenied -> {
            PermissionContent(
                status = PermissionStatus.PermanentlyDenied,
                onRetry = {}
            )
        }

        CreateMarkerPermissionState.Requesting -> {
            PermissionContent(
                status = PermissionStatus.Unknown,
                onRetry = permissionManager.requestPermissions
            )
        }
    }
}