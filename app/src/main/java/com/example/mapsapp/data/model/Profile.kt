package com.example.mapsapp.data.model

import kotlinx.serialization.Serializable

/**
 * Data model that represents a user profile stored in Supabase.
 *
 * Each profile is linked to the authenticated user through the same id
 * used in auth.users.
 *
 * @property id Unique identifier of the user profile.
 * @property name Visible display name of the user.
 * @property email Email associated with the authenticated account.
 * @property avatar_url Optional public URL of the user's profile image.
 */
@Serializable
data class Profile(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val avatar_url: String? = null
)