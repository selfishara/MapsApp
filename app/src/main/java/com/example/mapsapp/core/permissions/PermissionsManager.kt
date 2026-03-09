package com.example.mapsapp.core.permissions
/*La lògica relacionada amb la petició de permisos la centralitzarem a l’arxiu PermissionManager, en el package permissions.*/
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


//serveix per saber quins permisos tenim, i quina acció podem executar
data class PermissionManagerState(
    val status: PermissionStatus,
    val requestPermissions: () -> Unit
)


@Composable
// retornarà un PermissionManagerState que conté l’estat actual i l’acció que pot executar la UI.
fun rememberPermissionManager(permission: AppPermission): PermissionManagerState {
    val context = LocalContext.current
    val activity = context as? Activity

    //A la variable status observarem l’estat del permís, que cambiarà quan l’usuari el concedeix o el denega (parcial o permanentment).
    var status by remember {mutableStateOf<PermissionStatus>(PermissionStatus.Unknown)}

    //La funció rememberLauncherForActivityResult és el mecanisme per demanar permisos que ofereix Compose.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { results ->
        val allGranted = results.values.all { it }
        status = when {
            allGranted -> PermissionStatus.Granted
            activity != null && permission.permissions.any {
                //per decidir si s’ha de mostrar el quadre de diàleg per demanar permisos a l’usuari (si l’estat és PermanentlyDenied, no s’ha de tornar a mostrar).
                ActivityCompat.shouldShowRequestPermissionRationale(activity,it)
            } -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
    }

    //La funció requestPermissions llança el quadre de diàleg per demanar permisos.
    fun requestPermissions() {
        launcher.launch(permission.permissions.toTypedArray())
    }
    LaunchedEffect(Unit) {
        //comprova si tots els permisos han estat concedits
        val allGranted = permission.permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        status = if (allGranted) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Unknown
        }
    }
    return PermissionManagerState(
        status = status,
        requestPermissions = ::requestPermissions
    )
}

