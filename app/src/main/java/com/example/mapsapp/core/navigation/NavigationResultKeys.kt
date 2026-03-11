package com.example.mapsapp.core.navigation

/**
 * Keys used to communicate one-time navigation results between screens.
 *
 * These values are stored in the navigation SavedStateHandle and are
 * consumed when the destination screen becomes visible again.
 */
object NavigationResultKeys {

    /**
     * Snackbar message key used to show one-time feedback
     * after returning to a previous screen.
     */
    const val SNACKBAR_MESSAGE = "snackbar_message"
}