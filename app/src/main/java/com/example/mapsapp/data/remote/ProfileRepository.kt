package com.example.mapsapp.data.remote

import com.example.mapsapp.data.model.Profile
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

/**
 * Repository responsible for all CRUD operations related to user profiles.
 *
 * Profiles are stored in the "profiles" table in Supabase and linked
 * to the authenticated user through the same id as auth.users.
 *
 * @param postgrest Supabase Postgrest instance used for database operations.
 */
class ProfileRepository(
    private val postgrest: Postgrest
) {

    /**
     * Retrieves a profile from the database using the authenticated user id.
     *
     * @param userId Identifier of the authenticated user.
     * @return Profile entity stored in Supabase.
     */
    suspend fun getProfileById(userId: String): Profile {
        return postgrest
            .from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle()
    }

    /**
     * Inserts a new profile into the database.
     *
     * This function sends a new profile object to Supabase PostgREST
     * and stores it in the "profiles" table.
     *
     * @param profile Profile entity that will be stored in the database.
     */
    suspend fun insertProfile(profile: Profile) {
        postgrest
            .from("profiles")
            .insert(profile)
    }

    /**
     * Updates the information of an existing profile.
     *
     * This function modifies the name, email and avatar image URL
     * associated with a profile already stored in the database.
     *
     * @param userId Identifier of the authenticated user profile to update.
     * @param name Updated visible display name.
     * @param email Updated email value.
     * @param avatarUrl Optional updated profile image URL stored in Supabase Storage.
     */
    suspend fun updateProfile(
        userId: String,
        name: String?,
        email: String?,
        avatarUrl: String?
    ) {
        postgrest
            .from("profiles")
            .update({
                set("name", name)
                set("email", email)
                set("avatar_url", avatarUrl)
            }) {
                filter {
                    eq("id", userId)
                }
            }
    }
}