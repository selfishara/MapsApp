package com.example.mapsapp.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mapsapp.core.navigation.Destination

/**
 * Drawer menu composable used as the lateral navigation panel of the app.
 *
 * This component:
 * - renders a branded header
 * - shows all [DrawerItem] entries
 * - highlights the selected destination
 * - notifies the parent composable when a destination is selected
 *
 * The drawer itself does not navigate directly. It only sends the selected
 * destination through the [onNavigate] callback.
 *
 * @param currentRoute Current active navigation route used to highlight
 * the selected drawer item.
 * @param onNavigate Callback invoked when the user selects a drawer destination.
 */
@Composable
fun DrawerMenu(
    currentRoute: String?,
    onNavigate: (Destination) -> Unit
) {
    ModalDrawerSheet {

        Spacer(modifier = Modifier.height(20.dp))

        /**
         * Branded header of the drawer.
         */
        Text(
            text = "📍 PinPoint",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        Text(
            text = "Save your places beautifully",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        /**
         * Drawer entries rendered from [DrawerItem].
         */
        DrawerItem.entries.forEach { item ->

            /**
             * Indicates whether this item corresponds to
             * the currently visible destination.
             */
            val selected = currentRoute == item.destination.route

            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.text
                    )
                },
                label = {
                    Text(
                        text = item.text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                selected = selected,
                onClick = { onNavigate(item.destination) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}