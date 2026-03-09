package com.example.mapsapp.core.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.core.navigation.Destination

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val destination: Destination
) {
    MAPS(Icons.Default.Map, "Maps", Destination.Maps),
   SETTINGS(Icons.Default.Settings, "Settings", Destination.Settings),
   ABOUT(Icons.Default.Info, "About", Destination.About)
}