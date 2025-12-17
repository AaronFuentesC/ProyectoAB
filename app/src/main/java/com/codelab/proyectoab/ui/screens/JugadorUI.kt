package com.codelab.proyectoab.ui.screens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Immutable
data class JugadorUI(
    val jugador: Jugador,
    var isExpanded: MutableState<Boolean> = mutableStateOf(false)
)