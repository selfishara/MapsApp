package com.example.mapsapp.features.marker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mapsapp.features.marker.CreateMarkerViewModel

/**
 * Screen used to create a new map marker.
 *
 * The user can:
 * - add title
 * - add description
 * - upload image
 * - save marker in database
 *
 * @param navController Navigation controller used to return to the map.
 * @param latitude Latitude received from the map long press.
 * @param longitude Longitude received from the map long press.
 * @param viewModel ViewModel responsible for marker creation.
 */
@Composable
fun CreateMarkerScreen(
    navController: NavController,
    latitude: Double,
    longitude: Double,
    viewModel: CreateMarkerViewModel = viewModel()
) {

    val title by viewModel.title
    val description by viewModel.description
    val imageUri by viewModel.imageUri
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val creationSuccess by viewModel.creationSuccess

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.setImageUri(it) }
        }

    /**
     * When marker creation is completed successfully, return to the previous
     * screen and consume the success state so it is not triggered again.
     */
    LaunchedEffect(creationSuccess) {
        if (creationSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(
                    com.example.mapsapp.core.navigation.NavigationResultKeys.SNACKBAR_MESSAGE,
                    "Marker created successfully"
                )

            navController.popBackStack()
            viewModel.consumeCreationSuccess()
        }
    }

    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { viewModel.clearErrorMessage() },
            title = { Text("Error") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearErrorMessage() }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Create marker 📍",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Save a place on the map and add your own notes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        Text(
            text = "LAT: $latitude",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "LONG: $longitude",
            style = MaterialTheme.typography.bodySmall
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Marker image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                OutlinedButton(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select image")
                }
            }
        }

        TextField(
            value = title,
            onValueChange = { viewModel.editTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        TextField(
            value = description,
            onValueChange = { viewModel.editDescription(it) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(16.dp)
        )

        if (isLoading) {

            CircularProgressIndicator()

        } else {

            Button(
                onClick = {
                    viewModel.createMarker(latitude, longitude)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save marker")
            }
        }
    }
}