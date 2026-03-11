package com.example.mapsapp.features.profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.model.Profile
import com.example.mapsapp.data.remote.ProfileRepository
import com.example.mapsapp.data.remote.StorageRepository
import com.example.mapsapp.utils.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for loading and updating the authenticated user's profile.
 *
 * It handles:
 * - profile loading from Supabase
 * - local editable state for name and avatar
 * - optional profile image upload to Supabase Storage
 * - profile insert / update in the "profiles" table
 *
 * @param application Application context required for reading image input streams.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(MyApp.database)
    private val profileRepository = ProfileRepository(MyApp.database.postgrest)
    private val storageRepository = StorageRepository(MyApp.database.storage)

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _avatarUrl = mutableStateOf<String?>(null)
    val avatarUrl: State<String?> = _avatarUrl

    private val _selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageUri: State<Uri?> = _selectedImageUri

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    /**
     * Updates the editable name field.
     *
     * @param value New name entered by the user.
     */
    fun editName(value: String) {
        _name.value = value
    }

    /**
     * Stores the image selected by the user from the gallery.
     *
     * @param uri Uri of the selected image.
     */
    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    /**
     * Clears the current error message after it has been displayed.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /**
     * Consumes the save success state so it does not trigger repeatedly.
     */
    fun consumeSaveSuccess() {
        _saveSuccess.value = false
    }

    /**
     * Loads the authenticated user's profile from Supabase.
     *
     * If the profile does not exist yet, a new one is inserted automatically
     * using the current authenticated user id and email.
     */
    fun loadProfile() {
        val currentUserId = authRepository.currentUserId()
        val currentSession = authRepository.currentSession()

        if (currentUserId == null) {
            _errorMessage.value = "No authenticated user found."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val profile = try {
                    profileRepository.getProfileById(currentUserId)
                } catch (_: Exception) {
                    val newProfile = Profile(
                        id = currentUserId,
                        name = "",
                        email = currentSession?.user?.email ?: "",
                        avatar_url = null
                    )
                    profileRepository.insertProfile(newProfile)
                    newProfile
                }

                _name.value = profile.name.orEmpty()
                _email.value = profile.email.orEmpty()
                _avatarUrl.value = profile.avatar_url
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while loading the profile."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Saves the current profile changes in Supabase.
     *
     * If the user selected a new image, it is uploaded first to Storage
     * and its public URL is stored in the "profiles" table.
     */
    fun saveProfile() {
        val currentUserId = authRepository.currentUserId()

        if (currentUserId == null) {
            _errorMessage.value = "No authenticated user found."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val finalAvatarUrl = _selectedImageUri.value?.let { uri ->
                    getApplication<Application>()
                        .contentResolver
                        .openInputStream(uri)
                        ?.use { inputStream ->
                            val imageBytes = inputStream.readBytes()
                            storageRepository.uploadImage(imageBytes)
                        }
                } ?: _avatarUrl.value

                profileRepository.updateProfile(
                    userId = currentUserId,
                    name = _name.value.trim(),
                    email = _email.value.trim(),
                    avatarUrl = finalAvatarUrl
                )

                _avatarUrl.value = finalAvatarUrl
                _saveSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while saving the profile."
            } finally {
                _isLoading.value = false
            }
        }
    }
}