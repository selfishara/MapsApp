package com.example.mapsapp.features.marker

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.core.permissions.PermissionStatus
import com.example.mapsapp.data.model.MapMarker
import com.example.mapsapp.data.remote.MapMarkersRepository
import com.example.mapsapp.data.remote.StorageRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for the marker creation flow.
 *
 * It handles:
 * - permission state
 * - image selection (camera / gallery)
 * - marker form data
 * - image upload to Supabase Storage
 * - marker insertion into Supabase Database
 */
class CreateMarkerViewModel(application: Application) : AndroidViewModel(application) {

    private val mapMarkersRepository = MapMarkersRepository(MyApp.database.postgrest)
    private val storageRepository = StorageRepository(MyApp.database.storage)

    // -------------------------
    // PERMISOS (lo que ya tenías)
    // -------------------------
    private val _uiState = mutableStateOf<CreateMarkerPermissionState>(
        CreateMarkerPermissionState.Requesting
    )
    val uiState: State<CreateMarkerPermissionState> = _uiState

    fun onPermissionResult(status: PermissionStatus) {
        _uiState.value = when (status) {
            PermissionStatus.Granted -> CreateMarkerPermissionState.NavigateToCreateMarker
            PermissionStatus.Denied -> CreateMarkerPermissionState.ShowDenied
            PermissionStatus.PermanentlyDenied -> CreateMarkerPermissionState.ShowPermanentlyDenied
            PermissionStatus.Unknown -> CreateMarkerPermissionState.Requesting
        }
    }

    // -------------------------
    // IMAGEN / DIÁLOGO / LOADING (PDF)
    // -------------------------
    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _creationSuccess = mutableStateOf(false)
    val creationSuccess: State<Boolean> = _creationSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun consumeCreationSuccess() {
        _creationSuccess.value = false
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // ✅ URI temporal para cámara
    var tempUri: Uri? = null

    // -------------------------
    // DATOS DEL FORMULARIO
    // -------------------------
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _latitude = mutableStateOf<Double?>(null)
    val latitude: State<Double?> = _latitude

    private val _longitude = mutableStateOf<Double?>(null)
    val longitude: State<Double?> = _longitude

    fun editTitle(value: String) {
        _title.value = value
    }

    fun editDescription(value: String) {
        _description.value = value
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        _latitude.value = latitude
        _longitude.value = longitude
    }

    // -------------------------
    // FUNCIONES (las que te faltan)
    // -------------------------
    fun changeShowDialog(value: Boolean) {
        _showDialog.value = value
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun onCameraImageSaved() {
        _imageUri.value = tempUri
    }

    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun setCreationSuccess(value: Boolean) {
        _creationSuccess.value = value
    }

    /**
     * Creates a marker in Supabase.
     *
     * If an image has been selected, it is first uploaded to Storage and its
     * public URL is then stored in the database together with the marker data.
     *
     * @param userId Optional authenticated user id. It can be passed later
     * from the auth flow when everything is connected.
     */
    fun createMarker(userId: String? = null) {
        val currentTitle = _title.value.trim()
        val currentDescription = _description.value.trim()
        val currentLatitude = _latitude.value
        val currentLongitude = _longitude.value

        if (currentTitle.isBlank()) {
            _errorMessage.value = "Title cannot be empty."
            return
        }

        if (currentDescription.isBlank()) {
            _errorMessage.value = "Description cannot be empty."
            return
        }

        if (currentLatitude == null || currentLongitude == null) {
            _errorMessage.value = "Marker coordinates are missing."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val uploadedImageUrl = _imageUri.value?.let { uri ->
                    getApplication<Application>()
                        .contentResolver
                        .openInputStream(uri)
                        ?.use { inputStream ->
                            val imageBytes = inputStream.readBytes()
                            storageRepository.uploadImage(imageBytes)
                        }
                }

                val marker = MapMarker(
                    title = currentTitle,
                    description = currentDescription,
                    latitude = currentLatitude,
                    longitude = currentLongitude,
                    image_url = uploadedImageUrl,
                    user_id = userId
                )

                mapMarkersRepository.insertMarker(marker)
                _creationSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An unexpected error occurred while creating the marker."
            } finally {
                _isLoading.value = false
            }
        }
    }
}