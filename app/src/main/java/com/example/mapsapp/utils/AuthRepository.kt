package com.example.mapsapp.utils

import com.example.mapsapp.data.remote.MySupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository(
    private val supabase: MySupabaseClient
) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            supabase.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun register(email: String, password: String): AuthResult {
        return try {
            supabase.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al registrarse")
        }
    }

    suspend fun logout(): AuthResult {
        return try {
            supabase.client.auth.signOut()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error al cerrar sesión")
        }
    }

    fun currentSession() = supabase.client.auth.currentSessionOrNull()

    fun isLoggedIn(): Boolean {
        return supabase.client.auth.currentSessionOrNull() != null
    }
}