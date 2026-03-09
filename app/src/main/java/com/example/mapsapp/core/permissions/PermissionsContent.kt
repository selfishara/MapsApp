package com.example.mapsapp.core.permissions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PermissionContent(
    status: PermissionStatus,
    onRetry: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (status) {
            PermissionStatus.Unknown -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Requesting permission…")
            }

            PermissionStatus.Denied -> {
                Text("Permission denied")
                Spacer(Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Try again")
                }
            }

            PermissionStatus.PermanentlyDenied -> {
                Text("Permission permanently denied")
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }
                ) {
                    Text("Open settings")
                }
            }

            PermissionStatus.Granted -> Unit
        }
    }
}