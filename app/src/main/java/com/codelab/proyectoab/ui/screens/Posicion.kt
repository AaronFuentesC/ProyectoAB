package com.codelab.proyectoab.ui.screens

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.codelab.proyectoab.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

enum class Posicion {
    PORTERO,
    DEFENSA,
    CENTROCAMPISTA,
    DELANTERO
}
@Parcelize
data class Jugador(
    val id: Int,
    val nombre: String,
    val dorsal: Int,
    val posicion: Posicion,
    val edad: Int,
    val altura: Float,
    val peso: Float,
    val pais: String,
    val lesionado: Boolean,
    val goles: Int,
    val asistencias: Int,
    val partidos: Int,
    val icono:@RawValue ImageVector,
    val urlPerfil: String,
    val imagenId: Int
): Parcelable

@androidx.compose.runtime.Composable
fun Posicion.toLocalizedString(): String {
    return when (this) {
        Posicion.PORTERO -> stringResource(R.string.posicion_portero)
        Posicion.DEFENSA -> stringResource(R.string.posicion_defensa)
        Posicion.CENTROCAMPISTA -> stringResource(R.string.posicion_centrocampista)
        Posicion.DELANTERO -> stringResource(R.string.posicion_delantero)
    }
}