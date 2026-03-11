package com.example.mapsapp.core.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.core.navigation.Destination

/**
 * Enum that defines all entries displayed in the navigation drawer.
 *
 * Each drawer item contains:
 * - an icon
 * - a visible text label
 * - the destination opened when the item is pressed
 *
 * This enum is used by [DrawerMenu] to render the lateral menu.
 *
 * @property icon Icon displayed in the drawer entry.
 * @property text Visible label shown to the user.
 * @property destination App destination associated with the drawer item.
 */
enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val destination: Destination
) {

    /**
     * Main map screen.
     */
    MAPS(Icons.Default.Map, "Maps", Destination.Maps),

    /**
     * Screen that displays the markers created by the current user.
     */
    MY_MARKERS(Icons.Default.List, "My markers", Destination.MyMarkers),

    /**
     * Application settings screen.
     */
    SETTINGS(Icons.Default.Settings, "Settings", Destination.Settings),

    /**
     * About / project information screen.
     */
    ABOUT(Icons.Default.Info, "About", Destination.About)
}