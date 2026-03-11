package com.example.mapsapp.features.map.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mapsapp.data.model.MapMarker
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.google.maps.android.compose.clustering.Clustering

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    markers: List<MapMarker>,
    isDarkTheme: Boolean,
    onCreateMarkerRequest: (LatLng) -> Unit,
    onEditMarkerRequest: (Long) -> Unit
) {

    val itb = LatLng(41.4534225, 2.1837151)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itb, 17f)
    }

    val itbMarkerState = rememberUpdatedMarkerState(position = itb)

    var selectedMarker by remember { mutableStateOf<MapMarker?>(null) }

    val clusterItems = remember(markers) {
        markers.map { MarkerClusterItem(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,

            onMapClick = {
                selectedMarker = null
            },

            onMapLongClick = {
                onCreateMarkerRequest(it)
            }
        ) {

            Marker(
                state = itbMarkerState,
                title = "ITB",
                snippet = "Marker at ITB",
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_ROSE
                ),
                onClick = {
                    selectedMarker = null
                    false
                }
            )

            Clustering(
                items = clusterItems,
                onClusterItemClick = {
                    selectedMarker = it.marker
                    false
                }
            )
        }

        /* FAB */
        AnimatedVisibility(
            visible = true,
            enter = scaleIn() + fadeIn(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 16.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
                            if (selectedMarker != null) 240.dp else 16.dp                )
        ) {

            FloatingActionButton(
                onClick = {
                    onCreateMarkerRequest(cameraPositionState.position.target)
                },
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create marker")
            }
        }

        /* Marker card */
        AnimatedVisibility(
            visible = selectedMarker != null,
            enter = slideInVertically { it } + fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {

            selectedMarker?.let { marker ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(26.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        marker.image_url?.let {

                            AsyncImage(
                                model = it,
                                contentDescription = marker.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = marker.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = marker.description,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "LAT: ${marker.latitude}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "LONG: ${marker.longitude}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            marker.id?.let {

                                Button(
                                    onClick = {
                                        onEditMarkerRequest(it)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(4.dp)
                                ) {
                                    Text("Edit")
                                }
                            }

                            OutlinedButton(
                                onClick = { selectedMarker = null },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}