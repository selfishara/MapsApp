package com.example.mapsapp.features.map.ui

import com.example.mapsapp.data.model.MapMarker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Cluster item used to group map markers visually on the map.
 *
 * @property marker Original application marker.
 */
data class MarkerClusterItem(
    val marker: MapMarker
) : ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(marker.latitude, marker.longitude)
    }

    override fun getTitle(): String {
        return marker.title
    }

    override fun getSnippet(): String {
        return marker.description
    }

    override fun getZIndex(): Float? {
        return null
    }
}