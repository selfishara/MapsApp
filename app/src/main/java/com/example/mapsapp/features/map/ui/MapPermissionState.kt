package com.example.mapsapp.features.map.ui;
/*
* Aquesta clase representa l’estat de la UI de la pantalla del mapa, no l’estat del permís.
Amb ella traduirem PermissionStatus a un comportament de la UI.
*/
sealed class MapPermissionState {
    data object NavigateToMap : MapPermissionState()
    data object ShowDenied : MapPermissionState()
    data object ShowPermanentlyDenied : MapPermissionState()
    data object Requesting : MapPermissionState()
}