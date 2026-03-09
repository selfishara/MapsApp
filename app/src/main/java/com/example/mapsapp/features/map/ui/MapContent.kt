package com.example.mapsapp.features.map.ui

/*
 * (only map + markers + clicks)
 */
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mapsapp.data.model.MapMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState


/**
 * Displays the Google Map and handles basic map interactions.
 *
 * @param modifier Modifier applied to the root layout.
 * @param onCreateMarkerRequest Callback triggered when the user performs
 * a long click on the map and wants to create a new marker.
 */
@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    markers: List<MapMarker>,
    onCreateMarkerRequest: (LatLng) -> Unit
) {
    Column(modifier.fillMaxSize()) {

        // Initial coordinates (ITB)
        val itb = LatLng(41.4534225, 2.1837151)

        // Camera state (initial position + zoom)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }

        val itbMarkerState = rememberUpdatedMarkerState(position = itb)

        GoogleMap(
            modifier.fillMaxSize(),

            // Camera control
            cameraPositionState = cameraPositionState,

            // Optional - Link pdf

            // Allows map buttons (zoom, location...)
            // uiSettings = MapUiSettings(
            //     zoomControlsEnabled = true,
            //     myLocationButtonEnabled = true
            // ),

            // Map properties (requires permissions if location is enabled)
            // properties = MapProperties(
            //     isMyLocationEnabled = true
            // ),

            // Map events
            onMapClick = {
                Log.d("MAP_CLICKED", it.toString())
            },
            onMapLongClick = { latLng ->
                Log.d("MAP_LONG_CLICKED", latLng.toString())
                onCreateMarkerRequest(latLng)
            }
        ) { // Example marker from the pdf
            Marker(
                state = itbMarkerState,
                title = "ITB",
                snippet = "Marker at ITB"
            )

            // Real markers loaded from Supabase
            markers.forEach { marker ->
                val position = LatLng(marker.latitude, marker.longitude)

                Marker(
                    state = rememberUpdatedMarkerState(position = position),
                    title = marker.title,
                    snippet = marker.description
                )
            }
        }
    }
}