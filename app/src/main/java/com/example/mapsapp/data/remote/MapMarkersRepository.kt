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
     * Retrieves a marker from the database using its identifier.
     *
     * @param id Identifier of the marker to retrieve.
     * @return Marker entity stored in Supabase.
     */
    suspend fun getMarkerById(id: Long): MapMarker {
        return postgrest
            .from("posts")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle()
    }

    /**
     * Inserts a new marker into the database.
     *
     * This function sends a new marker object to Supabase PostgREST
     * and stores it in the "posts" table.
     *
     * @param marker Marker entity that will be stored in the database.
     */
    suspend fun insertMarker(marker: MapMarker) {
        postgrest
            .from("posts")
            .insert(marker)
    }

    /**
     * Updates the information of an existing marker.
     *
     * This function modifies the title, description and image
     * associated with a marker already stored in the database.
     *
     * @param id Identifier of the marker to update.
     * @param title Updated marker title.
     * @param description Updated marker description.
     * @param imageUrl Optional updated image URL stored in Supabase Storage.
     */
    suspend fun updateMarker(
        id: Long,
        title: String,
        description: String,
        imageUrl: String?
    ) {
        postgrest
            .from("posts")
            .update({
                set("title", title)
                set("description", description)
                set("image_url", imageUrl)
            }) {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Deletes a marker from the database.
     *
     * The marker is removed from the "posts" table using its identifier.
     *
     * @param id Identifier of the marker that will be deleted.
     */
    suspend fun deleteMarker(id: Long) {
        postgrest
            .from("posts")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Returns all markers created by a specific user.
     *
     * @param userId Authenticated user identifier.
     * @return List of markers belonging to that user.
     */
    suspend fun getMarkersByUserId(userId: String): List<MapMarker> {
        return postgrest
            .from("posts")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<MapMarker>()
    }
}