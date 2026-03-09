package com.example.mapsapp.data.remote

import io.github.jan.supabase.storage.Storage
import java.util.UUID

/**
 * Repository responsible for Supabase Storage operations.
 *
 * It uploads images to the "images" bucket and returns their public URL
 * so they can be associated with a map marker in the database.
 *
 * @param storage Supabase Storage instance.
 */
class StorageRepository(
    private val storage: Storage
) {

    /**
     * Uploads an image to the "images" bucket and returns its public URL.
     *
     * @param imageFile Image data as ByteArray.
     * @return Public URL of the uploaded image.
     */
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
        val bucket = storage.from("images") // Nom del bucket
        bucket.upload(path = fileName, data = imageFile)
        return bucket.publicUrl(fileName)
    }

    /**
     * Deletes an image from the "images" bucket.
     *
     * @param imageName Stored image file name.
     */
    suspend fun deleteImage(imageName: String) {
        val bucket = storage.from("images")
        bucket.delete(imageName)
    }
}