package com.codelab.proyectoab.ui.screens

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codelab.proyectoab.MainActivity
import com.codelab.proyectoab.R
import com.codelab.proyectoab.SeleccionaJugadorActivity

@Composable
fun InicioScreen(
    navController: NavController,
    prefs: SharedPreferences
) {
    val temaOscuro = prefs.getBoolean(MainActivity.CLAVE_TEMA_OSCURO, false)

    // Nombre guardado en prefs (fuente de verdad real)
    val nombreGuardado = prefs.getString(MainActivity.CLAVE_NOMBRE_USUARIO, "") ?: ""

    // Nombre que se edita
    var nombreTemporal by remember { mutableStateOf(nombreGuardado) }

    // Controla si el usuario ya guardó un nombre alguna vez
    var nombreGuardadoAlgunaVez by remember {
        mutableStateOf(nombreGuardado.isNotEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // -------- ESTADO EDITAR NOMBRE --------
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
            // -------- ESTADO SALUDO --------

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

        // -------- CONTENIDO VISUAL Y NAVEGACIÓN --------

        Image(
            modifier = Modifier.height(120.dp).width(120.dp),
            painter = painterResource(R.drawable.albacete_balompie),
            contentDescription = "Imagen del Albacete Balompié"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.titulo_inicio),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.descripcion_inicio),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("plantilla") },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(text = stringResource(R.string.boton_ver_plantilla))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------- BOTONES DE TEMA --------

        Button(
            onClick = {
                prefs.edit().putBoolean(MainActivity.CLAVE_TEMA_OSCURO, false).apply()
                (navController.context as MainActivity).recreate()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Modo Claro")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                prefs.edit().putBoolean(MainActivity.CLAVE_TEMA_OSCURO, true).apply()
                (navController.context as MainActivity).recreate()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Modo Oscuro")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (temaOscuro) "Modo: OSCURO" else "Modo: CLARO",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        val context = LocalContext.current
        Button(
            onClick = {
                val intent = Intent(context, SeleccionaJugadorActivity::class.java)
                (context as MainActivity).seleccionJugadorLauncher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar jugador")
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
