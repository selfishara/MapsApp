package com.example.mapsapp.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a marker created by a user and stored in Supabase.
 *
 * Each marker contains its map position, textual information and,
 * optionally, the public URL of an uploaded image.
 */
@Serializable
data class MapMarker(
    val id: Long? = null,
    val created_at: String? = null,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val image_url: String? = null,
    val user_id: String? = null
)