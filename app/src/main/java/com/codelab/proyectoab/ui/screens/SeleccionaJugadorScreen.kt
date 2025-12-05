package com.codelab.proyectoab.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items


@Composable
fun SeleccionaJugadorScreen(
    jugadores: List<Jugador>,
    onJugadorSeleccionado: (Jugador) -> Unit
) {
    LazyColumn {
        items(jugadores) { jugador ->
            ListItem(
                headlineContent = { Text(jugador.nombre) },
                modifier = Modifier.clickable {
                    onJugadorSeleccionado(jugador)
                }
            )
        }
    }
}