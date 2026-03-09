package com.example.mapsapp.core.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mapsapp.core.components.DrawerMenu
import com.example.mapsapp.core.navigation.Destination
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(navController: NavHostController,
                 content: @Composable () -> Unit
) { //navController o navHostController (especifico de compose)

    //estado del drawer (abierto/cerrado)
    val drawerState = rememberDrawerState(DrawerValue.Closed) //guarda el estado del drawer (menú lateral - abierto/cerrado)

    //scope para lanzar corrutinas (obligatorio para abrir/cerrar el drawer)
    val scope = rememberCoroutineScope()

    //ruta actual (para marcar el item seleccionado en el drawer)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    //contenedor del drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, //habilita gestos para abrir/cerrar el drawer (swipe)
        drawerContent = {
            DrawerMenu(
                currentRoute = currentRoute,
                onNavigate = { destination ->

                    //navegación real
                    navController.navigate(destination.route) {

                        //elimina de la pila la pantalla de destino para no acumular pantallas
                        popUpTo(Destination.Maps.route) {
                            inclusive = false
                            saveState = true
                        }
                        //no borra la pantalla Maps (raíz) de la pila

                        //evita que se dupliquen pantallas en la pila
                        launchSingleTop = true
                        restoreState = true

                    }
                    //corrutina para cerrar drawerMenu tras navegar
                    scope.launch { drawerState.close() }
                }
            )

        }) {
        //scaffold principal de la app (TopBar + content) con botón que abre el Drawer
        Scaffold(topBar = {
            TopAppBar(
                title = { Text("Mi MapsApp") },
                navigationIcon = {
                    IconButton(
                        //abrir el drawer (corrutina obligatoria)
                        onClick = { scope.launch { drawerState.open()
                        }
                        }) { Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú"
                    )
                    }
                }
            )
        }
        ) { padding ->
            Box(Modifier.padding(padding)) { content()}
        }

    }
}
