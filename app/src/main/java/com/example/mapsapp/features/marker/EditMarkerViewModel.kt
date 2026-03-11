package com.example.mapsapp.features.marker

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.remote.MapMarkersRepository
import com.example.mapsapp.data.remote.StorageRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for loading and updating an existing marker.
 *
 * It handles:
 * - marker loading by id
 * - editable title and description
 * - optional new image upload
 * - marker update in Supabase
 */
class EditMarkerViewModel(application: Application) : AndroidViewModel(application) {

    private val mapMarkersRepository = MapMarkersRepository(MyApp.database.postgrest)
    private val storageRepository = StorageRepository(MyApp.database.storage)

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    private val _existingImageUrl = mutableStateOf<String?>(null)
    val existingImageUrl: State<String?> = _existingImageUrl

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _updateSuccess = mutableStateOf(false)
    val updateSuccess: State<Boolean> = _updateSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun editTitle(value: String) {
        _title.value = value
    }

    fun editDescription(value: String) {
        _description.value = value
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun consumeUpdateSuccess() {
        _updateSuccess.value = false
    }

    /**
     * Loads marker information into the editable state.
     *
     * @param markerId Identifier of the marker to load.
     */
    fun loadMarker(markerId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val marker = mapMarkersRepository.getMarkerById(markerId)
                _title.value = marker.title
                _description.value = marker.description
                _existingImageUrl.value = marker.image_url
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while loading the marker."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates an existing marker in Supabase.
     *
     * If a new image has been selected, it is uploaded first and the new
     * public url is saved in the database.
     *
     * @param markerId Marker identifier.
     */
    fun updateMarker(markerId: Long) {
        val currentTitle = _title.value.trim()
        val currentDescription = _description.value.trim()

        if (currentTitle.isBlank()) {
            _errorMessage.value = "Title cannot be empty."
            return
        }

        if (currentDescription.isBlank()) {
            _errorMessage.value = "Description cannot be empty."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val finalImageUrl = _imageUri.value?.let { uri ->
                    getApplication<Application>()
                        .contentResolver
                        .openInputStream(uri)
                        ?.use { inputStream ->
                            val imageBytes = inputStream.readBytes()
                            storageRepository.uploadImage(imageBytes)
                        }
                } ?: _existingImageUrl.value

                mapMarkersRepository.updateMarker(
                    id = markerId,
                    title = currentTitle,
                    description = currentDescription,
                    imageUrl = finalImageUrl
                )

                _updateSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while updating the marker."
            } finally {
                _isLoading.value = false
            }
        }
    }
}