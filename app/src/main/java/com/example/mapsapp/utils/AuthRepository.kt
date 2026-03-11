package com.example.mapsapp.utils

import com.example.mapsapp.data.remote.MySupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

/**
 * Repository responsible for Supabase Authentication operations.
 *
 * It centralizes:
 * - login
 * - register
 * - logout
 * - current session access
 * - current authenticated user information
 *
 * @param supabase Shared Supabase client wrapper.
 */
class AuthRepository(
    private val supabase: MySupabaseClient
) {

    /**
     * Attempts to sign in the user with email and password.
     *
     * @param email User email.
     * @param password User password.
     * @return [AuthResult.Success] if login succeeds, or [AuthResult.Error] otherwise.
     */
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    /**
     * Attempts to register a new user with email and password.
     *
     * @param email User email.
     * @param password User password.
     * @return [AuthResult.Success] if registration succeeds, or [AuthResult.Error] otherwise.
     */
    suspend fun register(email: String, password: String): AuthResult {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al registrarse")
        }
    }

    /**
     * Closes the current authenticated session.
     *
     * @return [AuthResult.Success] if logout succeeds, or [AuthResult.Error] otherwise.
     */
    suspend fun logout(): AuthResult {
        return try {
            supabase.auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al cerrar sesión")
        }
    }

    /**
     * Returns the current active Supabase session, if any.
     */
    fun currentSession() = supabase.auth.currentSessionOrNull()

    /**
     * Returns the current authenticated user id, if available.
     */
    fun currentUserId(): String? {
        return supabase.auth.currentSessionOrNull()?.user?.id
    }

    /**
     * Returns whether there is an active authenticated session.
     */
    fun isLoggedIn(): Boolean {
        return currentSession() != null
    }
}