package com.example.mapsapp.data.model

import kotlinx.serialization.Serializable

/**
 * Data model that represents a map marker stored in Supabase.
 *
 * @property id Unique identifier of the marker.
 * @property title Marker title.
 * @property description Marker description.
 * @property latitude Marker latitude.
 * @property longitude Marker longitude.
 * @property image_url Optional image URL associated with the marker.
 * @property user_id Authenticated user identifier that owns the marker.
 * @property is_favorite Indicates whether the marker is marked as favorite.
 */
@Serializable
data class MapMarker(
    val id: Long? = null,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val image_url: String? = null,
    val user_id: String? = null,
    val is_favorite: Boolean = false
)