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
import com.example.mapsapp.features.marker.EditMarkerViewModel

/**
 * Screen used to edit an existing marker.
 *
 * The user can update title, description and image.
 *
 * @param navController Navigation controller used to return to the previous screen.
 * @param markerId Identifier of the marker being edited.
 * @param viewModel ViewModel responsible for marker editing.
 */
@Composable
fun EditMarkerScreen(
    navController: NavController,
    markerId: Long,
    viewModel: EditMarkerViewModel = viewModel()
) {
    val title by viewModel.title
    val description by viewModel.description
    val imageUri by viewModel.imageUri
    val existingImageUrl by viewModel.existingImageUrl
    val isLoading by viewModel.isLoading
    val updateSuccess by viewModel.updateSuccess
    val errorMessage by viewModel.errorMessage

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.setImageUri(it) }
        }

    LaunchedEffect(Unit) {
        viewModel.loadMarker(markerId)
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(
                    com.example.mapsapp.core.navigation.NavigationResultKeys.SNACKBAR_MESSAGE,
                    "Marker updated successfully"
                )

            navController.popBackStack()
            viewModel.consumeUpdateSuccess()
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

    val imageModel = imageUri ?: existingImageUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Edit marker",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Update your marker information and keep your saved places organised.",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                imageModel?.let { model ->
                    AsyncImage(
                        model = model,
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
                    Text("Change image")
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
                onClick = { viewModel.updateMarker(markerId) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save changes")
            }
        }
    }
}