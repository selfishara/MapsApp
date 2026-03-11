package com.example.mapsapp.features.map.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.core.permissions.PermissionStatus
import com.example.mapsapp.data.model.MapMarker
import com.example.mapsapp.data.remote.MapMarkersRepository
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for the map screen.
 *
 * It handles:
 * - map permission state
 * - marker loading from Supabase
 * - loading and error UI states related to marker retrieval
 * - marker filtering by title and description through a search query
 */
class MapViewModel : ViewModel() {

    private val mapMarkersRepository = MapMarkersRepository(MyApp.database.postgrest)

    private val _uiState = mutableStateOf<MapPermissionState>(MapPermissionState.Requesting)
    val uiState: State<MapPermissionState> = _uiState

    private val _markers = mutableStateOf<List<MapMarker>>(emptyList())
    val markers: State<List<MapMarker>> = _markers

    private val _isLoadingMarkers = mutableStateOf(false)
    val isLoadingMarkers: State<Boolean> = _isLoadingMarkers

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun onPermissionResult(status: PermissionStatus) {
        _uiState.value = when (status) {
            PermissionStatus.Granted -> MapPermissionState.NavigateToMap
            PermissionStatus.Denied -> MapPermissionState.ShowDenied
            PermissionStatus.PermanentlyDenied -> MapPermissionState.ShowPermanentlyDenied
            PermissionStatus.Unknown -> MapPermissionState.Requesting
        }
    }

    /**
     * Updates the current marker search query.
     *
     * @param value Text entered by the user in the search field.
     */
    fun editSearchQuery(value: String) {
        _searchQuery.value = value
    }

    /**
     * Loads all markers stored in Supabase.
     */
    fun loadMarkers() {
        viewModelScope.launch {
            _isLoadingMarkers.value = true
            _errorMessage.value = null

            try {
                _markers.value = mapMarkersRepository.getAllMarkers()
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while loading markers."
            } finally {
                _isLoadingMarkers.value = false
            }
        }
    }

    /**
     * Returns the list of markers filtered by the current search query.
     *
     * If the search query is blank, the full markers list is returned.
     * Otherwise, markers are matched against title and description.
     *
     * @return Filtered list of markers to display on the map.
     */
    fun getFilteredMarkers(): List<MapMarker> {
        val query = _searchQuery.value.trim()

        if (query.isBlank()) {
            return _markers.value
        }

        return _markers.value.filter { marker ->
            marker.title.contains(query, ignoreCase = true) ||
                    marker.description.contains(query, ignoreCase = true)
        }
    }

    /**
     * Clears the current marker loading error message.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}