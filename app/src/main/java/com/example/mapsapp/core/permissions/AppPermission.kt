package com.example.mapsapp.core.permissions

import android.Manifest

/**
 * centralitzarem la definició dels permisos que requereix la nostra app (geolocalització d’una banda, i càmera i gravació d’àudio per l’altra, tots dos junts)
 */

sealed class AppPermission(val permissions: List<String>) {

    object Location : AppPermission(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )

    object CameraAndAudio : AppPermission(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )
}