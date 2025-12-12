package com.codelab.proyectoab

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codelab.proyectoab.ui.screens.DetalleJugadorScreen
import com.codelab.proyectoab.ui.screens.InicioScreen
import com.codelab.proyectoab.ui.screens.PlantillaScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect


@Composable
fun MainScreen(prefs: SharedPreferences, initialJugadorId: Int = -1) {
    val navController = rememberNavController()
    // Navegar automáticamente si viene de una notificación
    LaunchedEffect(initialJugadorId) {
        if (initialJugadorId != -1) {
            navController.navigate("detalle_jugador/$initialJugadorId") {
// Opcional: evitar volver atrás a "inicio"
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            InicioScreen(navController = navController, prefs = prefs)
        }
        composable("plantilla") {
            PlantillaScreen(navController = navController,prefs = prefs)
        }
        composable(
            route = "detail_screen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            val jugador = RepositorioJugadores.getJugadorPorId(id)
            if (jugador != null) {
                DetalleJugadorScreen(jugador = jugador)
            } else {
                Text("Jugador no encontrado")
            }
        }
    }
}