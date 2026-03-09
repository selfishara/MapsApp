package com.example.mapsapp.core.permissions

/**
 * servirà per definir els possibles estats que pot tenir un permís
 */
sealed class PermissionStatus {

    object Unknown : PermissionStatus()

    object Granted : PermissionStatus()

    object Denied : PermissionStatus()

    object PermanentlyDenied : PermissionStatus()
}