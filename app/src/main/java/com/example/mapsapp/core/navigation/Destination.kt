package com.example.mapsapp.core.navigation

/**
 * Sealed class that defines all application navigation destinations.
 *
 * Each destination contains its base route used by the navigation graph.
 * Some destinations, such as [CreateMarker], include dynamic route
 * parameters that are filled at runtime.
 *
 * @property route Navigation route associated with the destination.
 */
sealed class Destination(val route: String) {

    /**
     * Initial screen used to decide whether the user
     * should go to login or directly to the map.
     */
    data object Splash : Destination("splash")

    /**
     * Authentication screens.
     */
    data object Login : Destination("login")
    data object Register : Destination("register")
    data object Logout : Destination("logout")

    /**
     * Main application screens.
     */
    data object Maps : Destination("maps")
    data object MyMarkers : Destination("my_markers")
    data object Profile : Destination("profile")
    data object Settings : Destination("settings")
    data object About : Destination("about")

    /**
     * Marker creation screen with dynamic latitude and longitude arguments.
     *
     * Route pattern:
     * create_marker/{latitude}/{longitude}
     */
    data object CreateMarker : Destination("create_marker/{latitude}/{longitude}") {

        /**
         * Builds the concrete navigation route for the marker creation screen.
         *
         * @param latitude Latitude selected on the map.
         * @param longitude Longitude selected on the map.
         * @return Route string with coordinates included.
         */
        fun createRoute(latitude: Double, longitude: Double): String {
            return "create_marker/$latitude/$longitude"
        }
    }

    data object EditMarker : Destination("edit_marker/{markerId}") {
        fun createRoute(markerId: Long): String {
            return "edit_marker/$markerId"
        }
    }
}