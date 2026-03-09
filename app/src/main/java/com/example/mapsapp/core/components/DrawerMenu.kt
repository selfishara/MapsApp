package com.example.mapsapp.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mapsapp.core.components.DrawerItem
import com.example.mapsapp.core.navigation.Destination

/**
 * DrawerMenu:
 * Composable que define el contenido del menú lateral (Drawer).
 *
 * @param currentRoute Ruta actual para marcar el item seleccionado.
 * @param onNavigate Callback que notifica al componente padre
 *                   qué destino ha seleccionado el usuario.
 */

@Composable
fun DrawerMenu(
    currentRoute: String?,
    onNavigate: (Destination) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Text(text = "Menú", modifier = Modifier.padding(16.dp))

        // Aqui van los items del drawer (DrawerItem enum)
        // Los recorremos
        DrawerItem.entries.forEach { item ->

            // Este item corresponde a la pantalla actual?
            // si es asi, lo marcamos como seleccionado
            val selected = currentRoute == item.destination.toString()

            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.text
                    )
                },
            label = { Text(text = item.text) },
                selected = selected,

                //aquí es donde "se ve" el onNavigate (el drawer no navega, solo avisa qué se Destination se ha pulsado)
                onClick = { onNavigate(item.destination)},

                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
        }
    }
}
