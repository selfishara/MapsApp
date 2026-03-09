package com.example.mapsapp.features.marker

sealed class CreateMarkerPermissionState {
    data object Requesting : CreateMarkerPermissionState()
    data object ShowDenied : CreateMarkerPermissionState()
    data object ShowPermanentlyDenied : CreateMarkerPermissionState()
    data object NavigateToCreateMarker : CreateMarkerPermissionState()
}