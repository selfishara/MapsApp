package com.example.mapsapp.features.map.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * About screen of the application.
 *
 * This screen provides general information about the project,
 * its purpose and the main technologies used in its development.
 */
@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("About MapsApp")

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Project")
                Text(
                    "MapsApp is an Android application that allows authenticated users " +
                            "to create map markers with a title, description and optional image."
                )
            }
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Main features")
                Text("• User authentication with Supabase")
                Text("• Interactive map with Google Maps")
                Text("• Marker creation with coordinates")
                Text("• Image upload to Supabase Storage")
                Text("• Marker persistence in Supabase Database")
            }
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Technologies")
                Text("• Kotlin")
                Text("• Jetpack Compose")
                Text("• Material 3")
                Text("• Google Maps Compose")
                Text("• Supabase Auth / Database / Storage")
            }
        }
    }
}