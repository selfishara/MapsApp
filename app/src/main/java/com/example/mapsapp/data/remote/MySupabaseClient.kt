package com.example.mapsapp.data.remote

import com.example.mapsapp.data.model.Student
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

/**
 * Central Supabase client wrapper used across the application.
 *
 * This class installs all the Supabase modules required by the project:
 * - Postgrest for database operations
 * - Auth for authentication and session persistence
 * - Storage for file uploads and deletions
 *
 * It also keeps the temporary CRUD methods used in the Student practice flow
 * so the existing files continue compiling while the final map marker flow is
 * being integrated.
 *
 * @param supabaseUrl Supabase project URL.
 * @param supabaseKey Supabase publishable / anon key.
 */
class MySupabaseClient(
    supabaseUrl: String,
    supabaseKey: String
) {

    /**
     * Shared Supabase client instance.
     */
    val client = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        // Database
        install(Postgrest)

        // Authentication
        install(Auth) {
            autoLoadFromStorage = true
        }

        // client Storage
        install(Storage)
    }

    /**
     * Postgrest entry point for database queries.
     */
    val postgrest = client.postgrest

    /**
     * Auth entry point for login / register / logout.
     */
    val auth = client.auth

    /**
     * Storage entry point for file operations.
     */
    val storage = client.storage

    // -------- STUDENT PRACTICE CRUD --------

    /**
     * Returns all students stored in the Student table.
     */
    suspend fun getAllStudents(): List<Student> {
        return postgrest
            .from("Student")
            .select()
            .decodeList<Student>()
    }

    /**
     * Returns a single student by id.
     *
     * @param id Student identifier.
     */
    suspend fun getStudent(id: String): Student {
        return postgrest
            .from("Student")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<Student>()
    }

    /**
     * Inserts a new student into the Student table.
     *
     * @param student Student to insert.
     */
    suspend fun insertStudent(student: Student) {
        postgrest
            .from("Student")
            .insert(student)
    }

    /**
     * Updates an existing student.
     *
     * @param id Student identifier.
     * @param name Updated name.
     * @param mark Updated mark.
     */
    suspend fun updateStudent(id: String, name: String, mark: Double) {
        postgrest
            .from("Student")
            .update({
                set("name", name)
                set("mark", mark)
            }) {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Deletes a student by id.
     *
     * @param id Student identifier.
     */
    suspend fun deleteStudent(id: String) {
        postgrest
            .from("Student")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}