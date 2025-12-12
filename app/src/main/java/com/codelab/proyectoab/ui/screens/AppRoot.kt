package com.codelab.proyectoab.ui.screens

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.rememberNavController

@Composable
fun AppRoot(prefs: SharedPreferences, initialJugadorId: Int) {
    val navController = rememberNavController()
// Restaurar navegación si viene de notificación
    LaunchedEffect(initialJugadorId) {
        if (initialJugadorId != -1) {
            navController.navigate("detalle_jugador/$initialJugadorId") {
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    val config = LocalConfiguration.current
    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        AppLandscape(navController = navController, prefs = prefs)
    } else {
        AppPortrait(navController = navController, prefs = prefs)
    }
}