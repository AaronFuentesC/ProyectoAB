package com.codelab.proyectoab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codelab.proyectoab.ui.screens.SeleccionaJugadorScreen
import com.codelab.proyectoab.ui.theme.ProyectoABTheme

class SeleccionaJugadorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoABTheme {
                SeleccionaJugadorScreen(
                    jugadores = RepositorioJugadores.getJugadores()
                ) { jugadorSeleccionado ->
                    val intent = Intent().apply {
                        putExtra("jugador_seleccionado", jugadorSeleccionado)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}