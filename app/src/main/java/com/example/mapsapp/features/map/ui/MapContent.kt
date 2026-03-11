package com.example.mapsapp.features.map.ui

/*
 * (only map + markers + clicks)
 */

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mapsapp.R
import com.example.mapsapp.data.model.MapMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

/**
 * Displays the Google Map and handles basic map interactions.
 *
 * @param modifier Modifier applied to the root layout.
 * @param markers List of markers loaded from Supabase.
 * @param isDarkTheme Indicates whether the app is currently using dark mode.
 * @param onCreateMarkerRequest Callback triggered when the user performs
 * a long click on the map or presses the floating action button.
 */
@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    markers: List<MapMarker>,
    isDarkTheme: Boolean,
    onCreateMarkerRequest: (LatLng) -> Unit
) {

    val context = LocalContext.current

    /**
     * Map properties adapted to the current app theme.
     *
     * When dark mode is active, a custom map style is applied.
     */
    val mapProperties = if (isDarkTheme) {
        MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map_style_dark
            )
        )
    } else {
        MapProperties()
    }

    // Initial coordinates (ITB)
    val itb = LatLng(41.4534225, 2.1837151)

    // Camera state (initial position + zoom)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itb, 17f)
    }

    val itbMarkerState = rememberUpdatedMarkerState(position = itb)

    /**
     * Currently selected marker.
     *
     * When a marker is tapped, a detail card is shown at the bottom
     * of the map with image, title, description and coordinates.
     */
    var selectedMarker by remember { mutableStateOf<MapMarker?>(null) }

    /**
     * Cluster items derived from the current markers list.
     */
    val clusterItems = remember(markers) {
        markers.map { marker ->
            MarkerClusterItem(marker)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),

            cameraPositionState = cameraPositionState,

            properties = mapProperties,

            onMapClick = {
                Log.d("MAP_CLICKED", it.toString())
                selectedMarker = null
            },

            onMapLongClick = { latLng ->
                Log.d("MAP_LONG_CLICKED", latLng.toString())
                onCreateMarkerRequest(latLng)
            }
        ) {

            /**
             * Example marker from the pdf.
             */
            Marker(
                state = itbMarkerState,
                title = "ITB",
                snippet = "Marker at ITB",
                onClick = {
                    selectedMarker = null
                    false
                }
            )

            /**
             * Real markers loaded from Supabase and rendered through clustering.
             */
            Clustering(
                items = clusterItems,
                onClusterItemClick = { clusterItem ->
                    selectedMarker = clusterItem.marker
                    false
                }
            )
        }

        /**
         * Floating action button used to create a marker from the current
         * center position of the map.
         *
         * This provides an alternative to the long click interaction.
         */
        FloatingActionButton(
            onClick = {
                val centerLatLng = cameraPositionState.position.target
                onCreateMarkerRequest(centerLatLng)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = if (selectedMarker != null) 220.dp else 16.dp
                ),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create marker"
            )
        }

        /**
         * Marker detail card shown when a marker is selected.
         */
        selectedMarker?.let { marker ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    marker.image_url?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = marker.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }

                    Text(
                        text = "📌 ${marker.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = marker.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )

                    Text(
                        text = "📍 LAT: ${marker.latitude}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "🧭 LONG: ${marker.longitude}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    TextButton(
                        onClick = { selectedMarker = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}