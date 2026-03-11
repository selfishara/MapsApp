package com.example.mapsapp.features.marker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.model.MapMarker
import com.example.mapsapp.data.remote.MapMarkersRepository
import com.example.mapsapp.utils.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for loading the markers created
 * by the currently authenticated user.
 */
class MyMarkersViewModel : ViewModel() {

    private val authRepository = AuthRepository(MyApp.database)
    private val mapMarkersRepository = MapMarkersRepository(MyApp.database.postgrest)

    private val _markers = mutableStateOf<List<MapMarker>>(emptyList())
    val markers: State<List<MapMarker>> = _markers

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    /**
     * Loads all markers that belong to the current authenticated user.
     */
    fun loadMyMarkers() {
        val currentUserId = authRepository.currentUserId()

        if (currentUserId == null) {
            _errorMessage.value = "No authenticated user found."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                _markers.value = mapMarkersRepository.getMarkersByUserId(currentUserId)
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while loading markers."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Deletes a marker created by the current user.
     *
     * After removing the marker from the database, the markers
     * list is reloaded so the UI reflects the change immediately.
     *
     * @param markerId Identifier of the marker to delete.
     */
    fun deleteMarker(markerId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                mapMarkersRepository.deleteMarker(markerId)
                loadMyMarkers()
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while deleting the marker."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears the current error message after it has been displayed in the UI.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}