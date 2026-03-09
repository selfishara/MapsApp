package com.example.mapsapp.features.marker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mapsapp.features.marker.CreateMarkerViewModel
import com.example.mapsapp.utils.FileUtils

/**
 * UI displayed once all marker creation permissions have been granted.
 *
 * This composable allows the user to:
 * - enter a title and description
 * - pick or capture an image
 * - upload the image to Supabase Storage
 * - create a marker in Supabase Database
 *
 * @param viewModel ViewModel responsible for the marker creation flow.
 */
@Composable
fun CreateMarkerContent(
    viewModel: CreateMarkerViewModel
) {
    val context = LocalContext.current

    val title by viewModel.title
    val description by viewModel.description
    val imageUri by viewModel.imageUri
    val showDialog by viewModel.showDialog
    val isLoading by viewModel.isLoading
    val latitude by viewModel.latitude
    val longitude by viewModel.longitude
    val errorMessage by viewModel.errorMessage

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.onCameraImageSaved()
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.setImageUri(it) }
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Create marker")

        latitude?.let { lat ->
            longitude?.let { lng ->
                Text("Coordinates: $lat, $lng")
            }
        }

        TextField(
            value = title,
            onValueChange = { viewModel.editTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { viewModel.editDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.changeShowDialog(true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add image")
        }

        imageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Selected marker image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.createMarker()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save marker")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.changeShowDialog(false) },
            title = { Text("Select image source") },
            text = { Text("Choose whether you want to take a photo or select one from the gallery.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.changeShowDialog(false)
                        val uri = FileUtils.createImageUri(context)
                        uri?.let {
                            viewModel.tempUri = it
                            takePictureLauncher.launch(it)
                        }
                    }
                ) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.changeShowDialog(false)
                        pickImageLauncher.launch("image/*")
                    }
                ) {
                    Text("Gallery")
                }
            }
        )
    }
}