package com.example.mapsapp.data.remote

import com.example.mapsapp.data.model.MapMarker
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

/**
 * Repository responsible for all CRUD operations related to map markers.
 *
 * Markers are stored in the "posts" table in Supabase.
 *
 * @param postgrest Supabase Postgrest instance used for database operations.
 */
class MapMarkersRepository(
    private val postgrest: Postgrest
) {

    /**
     * Returns all stored map markers.
     */
    suspend fun getAllMarkers(): List<MapMarker> {
        return postgrest
            .from("posts")
            .select()
            .decodeList<MapMarker>()
    }

    /**
     * Returns a single map marker by id.
     *
     * @param id Marker identifier.
     */
    suspend fun getMarker(id: String): MapMarker {
        return postgrest
            .from("posts")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<MapMarker>()
    }

    /**
     * Inserts a new marker into the posts table.
     *
     * @param marker Marker to insert.
     */
    suspend fun insertMarker(marker: MapMarker) {
        postgrest
            .from("posts")
            .insert(marker)
    }

    /**
     * Updates an existing marker.
     *
     * @param id Marker identifier.
     * @param title Updated title.
     * @param description Updated description.
     * @param latitude Updated latitude.
     * @param longitude Updated longitude.
     * @param image_url Optional public image URL.
     */
    suspend fun updateMarker(
        id: String,
        title: String,
        description: String,
        latitude: Double,
        longitude: Double,
        image_url: String?
    ) {
        postgrest
            .from("posts")
            .update({
                set("title", title)
                set("description", description)
                set("latitude", latitude)
                set("longitude", longitude)
                set("image_url", image_url)
            }) {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Deletes a marker by id.
     *
     * @param id Marker identifier.
     */
    suspend fun deleteMarker(id: String) {
        postgrest
            .from("posts")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}