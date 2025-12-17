package com.codelab.proyectoab.ui.screens

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.codelab.proyectoab.MainActivity


@Composable
fun ConfiguracionScreen(navController: NavController,
                    prefs: SharedPreferences){
    val navController = rememberNavController()
    val temaOscuro = prefs.getBoolean(MainActivity.CLAVE_TEMA_OSCURO, false)

    val nombreGuardado = prefs.getString(MainActivity.CLAVE_NOMBRE_USUARIO, "") ?: ""

    var nombreTemporal by remember { mutableStateOf(nombreGuardado) }

    var nombreGuardadoAlgunaVez by remember {
        mutableStateOf(nombreGuardado.isNotEmpty())
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!nombreGuardadoAlgunaVez) {

            OutlinedTextField(
                value = nombreTemporal,
                onValueChange = { nombreTemporal = it },
                label = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreTemporal.isNotBlank()) {
                        prefs.edit()
                            .putString(
                                MainActivity.CLAVE_NOMBRE_USUARIO,
                                nombreTemporal.trim()
                            )
                            .apply()

                        nombreGuardadoAlgunaVez = true
                    }
                },
                enabled = nombreTemporal.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar nombre")
            }

            Spacer(modifier = Modifier.height(16.dp))

        } else {

            val nombreRealGuardado =
                prefs.getString(MainActivity.CLAVE_NOMBRE_USUARIO, "") ?: ""

            Text(
                "¡Hola, $nombreRealGuardado!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    prefs.edit().remove(MainActivity.CLAVE_NOMBRE_USUARIO).apply()
                    nombreTemporal = ""
                    nombreGuardadoAlgunaVez = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar nombre")
            }
        }
    Button(
        onClick = {
            val activity = navController.context as MainActivity
            if (activity.usuarioTienePermisoContactos()) {
                Toast.makeText(activity, "Mostrando contactos...", Toast.LENGTH_SHORT).show()
            } else {
                activity.solicitarPermisoContactos()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Ver contactos del sistema")
    }
    }
}