package com.codelab.proyectoab

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.codelab.proyectoab.ui.theme.ProyectoABTheme

class MainActivity : ComponentActivity() {
    companion object {
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        const val CLAVE_TEMA_OSCURO = "tema_oscuro"
        const val CLAVE_JUGADORES_EXPANDIDOS = "jugadores_expandidos" // ← Nueva constante
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferences
        val prefs = getSharedPreferences("ajustes_usuario", Context.MODE_PRIVATE)
        val temaOscuro = prefs.getBoolean(CLAVE_TEMA_OSCURO, false)

        // Aplicar modo de noche antes de dibujar UI
        AppCompatDelegate.setDefaultNightMode(
            if (temaOscuro) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        enableEdgeToEdge()
        setContent {
            ProyectoABTheme(darkTheme = temaOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos prefs a la UI
                    MainScreen(prefs = prefs)
                }
            }
        }
    }
}
