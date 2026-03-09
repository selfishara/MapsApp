package com.example.mapsapp.core.navigation

sealed class Destination( val route: String) {
    data object Maps : Destination(route = "maps")
    data object Settings : Destination("settings")
    data  object About : Destination("about")

    // marker data object with dynamic route parameters for latitude and longitude
    data object CreateMarker : Destination("create_marker/{latitude}/{longitude}") {
        fun createRoute(latitude: Double, longitude: Double): String {
            return "create_marker/$latitude/$longitude"
        }
    }
}
