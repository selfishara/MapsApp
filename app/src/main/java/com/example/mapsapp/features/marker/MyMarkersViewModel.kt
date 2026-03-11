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
 * Filter options used in the My Markers screen.
 */
enum class MyMarkersFilter {
    ALL,
    FAVORITES
}

/**
 * ViewModel responsible for loading, filtering and updating the current user's markers.
 */
class MyMarkersViewModel : ViewModel() {

    private val mapMarkersRepository = MapMarkersRepository(MyApp.database.postgrest)
    private val authRepository = AuthRepository(MyApp.database)

    private val _markers = mutableStateOf<List<MapMarker>>(emptyList())
    val markers: State<List<MapMarker>> = _markers

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    private val _selectedFilter = mutableStateOf(MyMarkersFilter.ALL)
    val selectedFilter: State<MyMarkersFilter> = _selectedFilter

    /**
     * Returns the marker list filtered according to the selected filter.
     */
    fun getFilteredMarkers(): List<MapMarker> {
        return when (_selectedFilter.value) {
            MyMarkersFilter.ALL -> _markers.value
            MyMarkersFilter.FAVORITES -> _markers.value.filter { it.is_favorite }
        }
    }

    /**
     * Changes the selected filter in the My Markers screen.
     *
     * @param filter New filter to apply.
     */
    fun selectFilter(filter: MyMarkersFilter) {
        _selectedFilter.value = filter
    }

    /**
     * Loads the markers created by the authenticated user.
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
     * Toggles the favorite state of a marker.
     *
     * @param marker Marker whose favorite state should be updated.
     */
    fun toggleFavorite(marker: MapMarker) {
        val markerId = marker.id ?: return

        viewModelScope.launch {
            try {
                val newValue = !marker.is_favorite
                mapMarkersRepository.updateFavoriteState(markerId, newValue)

                _markers.value = _markers.value.map { current ->
                    if (current.id == markerId) {
                        current.copy(is_favorite = newValue)
                    } else {
                        current
                    }
                }

                _successMessage.value = if (newValue) {
                    "Added to favorites"
                } else {
                    "Removed from favorites"
                }
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while updating favorites."
            }
        }
    }

    /**
     * Deletes a marker created by the current user.
     *
     * @param markerId Identifier of the marker to delete.
     */
    fun deleteMarker(markerId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                mapMarkersRepository.deleteMarker(markerId)
                _markers.value = _markers.value.filterNot { it.id == markerId }
                _successMessage.value = "Marker deleted successfully"
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "An unexpected error occurred while deleting the marker."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}